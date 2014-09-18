package net.intelie.lognit.cli.runners;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.MessageBag;
import net.intelie.lognit.cli.model.SearchStats;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class BufferListener implements RestListener<MessageBag> {
    public static final String NO_CLUSTER_INFO = "(%s) WARN: seems there is a bug in server response, no cluster info";
    public static final String MISSING_NODES_RESPONSE = "WARN: missing some cluster responses, check nodes status";
    public static final String QUERY_CANCELLED = "(%s) WARN: %s";
    public static final String QUERY_INFO = "(%s) INFO: %s";
    public static final String RESPONSE_RECEIVED = "(%s) response %d/%d: %,d of %,d historic results in %dms";

    private final Semaphore errors;
    private final Deque<MessageBag> historic;
    private final Deque<MessageBag> other;
    private final Semaphore semaphore;
    private final Formatter printer;
    private final boolean printStats;
    private final boolean printMeta;
    private boolean releasing;
    private int historicCount = 0;

    public BufferListener(Formatter printer, boolean printStats, boolean printMeta) {
        this.errors = new Semaphore(0);
        this.printer = printer;
        this.printStats = printStats;
        this.printMeta = printMeta;
        this.historic = new LinkedList<MessageBag>();
        this.other = new LinkedList<MessageBag>();
        this.semaphore = new Semaphore(0);
        this.releasing = false;
    }


    @Override
    public synchronized void receive(MessageBag messages) {
        if (messages.isSuccess() && messages.isHistoric()) {
            printer.printStatus(RESPONSE_RECEIVED, messages.getNode(),
                    ++historicCount,
                    messages.getTotalNodes(),
                    messages.getItems().size(),
                    messages.getTotalItems(),
                    messages.getTime());
            if (!Strings.isNullOrEmpty(messages.getMessage()))
                printer.printStatus(QUERY_INFO, messages.getNode(), messages.getMessage());
        }
        if (releasing || !messages.isSuccess()) {
            printBag(messages);
        } else if (messages.isHistoric()) {
            historic.add(messages);
            semaphore.release();
        } else {
            other.add(messages);
        }
        if (!messages.isSuccess())
            errors.release();
    }

    public boolean waitHistoric(int timeout, int releaseMax) {
        boolean success = true;
        try {
            if (!waitForAnswer(1, timeout)) {
                printer.printStatus(MISSING_NODES_RESPONSE);
                return false;
            }
            MessageBag first = historic.getFirst();
            int waiting = first.getTotalNodes() - 1;
            if (waiting < 0) {
                printer.printStatus(NO_CLUSTER_INFO, first.getNode());
                success = false;
            } else if (!waitForAnswer(waiting, timeout)) {
                printer.printStatus(MISSING_NODES_RESPONSE);
                success = false;
            }
        } catch (InterruptedException ex) {
            printer.printStatus(MISSING_NODES_RESPONSE);
        }
        releaseHistoric(releaseMax);
        return success;
    }

    private boolean waitForAnswer(int howMany, int timeout) throws InterruptedException {
        return semaphore.tryAcquire(howMany, timeout, TimeUnit.MILLISECONDS);
    }

    public void waitForError(int n) {
        errors.acquireUninterruptibly(n);
    }

    private void releaseHistoric(int releaseMax) {
        if (printStats) {
            SearchStats merged = new SearchStats();
            for (MessageBag bag : historic)
                merged.merge(bag.getStats());
            printer.print(merged);
        } else {
            List<Message> reverse = pickValidHistory(releaseMax);

            for (Message message : reverse)
                printer.print(message, printMeta);
        }
    }

    private List<Message> pickValidHistory(int releaseMax) {
        PriorityQueue<Message> queue = new PriorityQueue<Message>();
        while (!historic.isEmpty())
            queue.addAll(historic.pop().getItems());

        LinkedList<Message> list = new LinkedList<Message>();
        while (!queue.isEmpty() && list.size() < releaseMax)
            list.addFirst(queue.poll());

        return list;
    }

    public synchronized void releaseAll() {
        releasing = true;
        while (!other.isEmpty()) {
            MessageBag bag = other.pop();
            printBag(bag);
        }
    }

    private void printBag(MessageBag bag) {
        if (bag.isSuccess()) {
            if (bag.getItems() != null)
                printMessages(bag.isHistoric(), bag.getItems());

            if (bag.getAggregated() != null)
                printAggregated(bag.getAggregated());
        } else {
            printer.printStatus(QUERY_CANCELLED, bag.getNode(), bag.getMessage());
        }
    }

    private void printAggregated(Aggregated aggregated) {
        printer.print(aggregated);
    }

    private void printMessages(boolean historic, List<Message> list) {
        if (historic) list = Lists.reverse(list);
        for (Message message : list)
            printer.print(message, printMeta);
    }

    public Formatter getFormatter() {
        return printer;
    }
}
