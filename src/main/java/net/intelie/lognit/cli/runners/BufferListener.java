package net.intelie.lognit.cli.runners;

import com.google.common.collect.Lists;
import net.intelie.lognit.cli.formatters.Formatter;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.MessageBag;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class BufferListener implements RestListener<MessageBag> {
    public static final String NO_CLUSTER_INFO = "WARN: seems there is a bug in server response, no cluster info";
    public static final String MISSING_NODES_RESPONSE = "WARN: missing some cluster responses, check nodes status";
    public static final String QUERY_CANCELLED = "WARN: %s";
    public static final String REPONSE_RECEIVED = "INFO: answer from %s, %d items (%dms)";
    private final Deque<MessageBag> historic;
    private final Deque<MessageBag> other;
    private final Semaphore semaphore;
    private final Formatter printer;
    private final boolean verbose;
    private boolean releasing;

    public BufferListener(Formatter printer, boolean verbose) {
        this.printer = printer;
        this.verbose = verbose;
        this.historic = new LinkedList<MessageBag>();
        this.other = new LinkedList<MessageBag>();
        this.semaphore = new Semaphore(0);
        this.releasing = false;
    }


    @Override
    public synchronized void receive(MessageBag messages) {
        if (verbose && messages.isSuccess() && messages.isHistoric()) {
            printer.printStatus(REPONSE_RECEIVED, messages.getNode(), messages.getItems().size(), messages.getTime());
        }
        if (releasing || !messages.isSuccess()) {
            printBag(messages);
            return;
        }
        if (messages.isHistoric()) {
            historic.add(messages);
            semaphore.release();
        } else {
            other.add(messages);
        }
    }

    public boolean waitHistoric(int timeout, int releaseMax) {
        boolean success = true;
        try {
            if (!waitForAnswer(1, timeout)) {
                printer.printStatus(MISSING_NODES_RESPONSE);
                return false;
            }
            int waiting = historic.getFirst().getTotalNodes() - 1;
            if (waiting < 0) {
                printer.printStatus(NO_CLUSTER_INFO);
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

    private void releaseHistoric(int releaseMax) {
        List<Message> reverse = pickValidHistory(releaseMax);

        for (Message message : reverse)
            printer.printMessage(message);
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
            printer.printStatus(QUERY_CANCELLED, bag.getMessage());
        }
    }

    private void printAggregated(Aggregated aggregated) {
        printer.printAggregated(aggregated);
    }

    private void printMessages(boolean historic, List<Message> list) {
        if (historic) list = Lists.reverse(list);
        for (Message message : list)
            printer.printMessage(message);
    }

    public Formatter getFormatter() {
        return printer;
    }

    public boolean isVerbose() {
        return verbose;
    }
}
