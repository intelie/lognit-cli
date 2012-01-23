package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.http.RestListener;
import net.intelie.lognit.cli.model.MessageBag;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class BufferListener implements RestListener<MessageBag> {
    private final Deque<MessageBag> historic;
    private final Deque<MessageBag> other;
    private final UserInput console;
    private final Semaphore semaphore;

    @Inject
    public BufferListener(UserInput console) {
        this.console = console;
        this.historic = new LinkedList<MessageBag>();
        this.other = new LinkedList<MessageBag>();
        this.semaphore = new Semaphore(0);
    }


    @Override
    public synchronized void receive(MessageBag messages) {
        if (messages.isHistoric()) {
            historic.add(messages);
            semaphore.release();
        } else {
            other.add(messages);
        }
    }

    public boolean waitAll(int timeout) {
        boolean success = false;
        try {
            if (!semaphore.tryAcquire(1, timeout, TimeUnit.MILLISECONDS)) return false;
            int waiting = historic.getFirst().getTotalNodes() - 1;
            success = semaphore.tryAcquire(waiting, timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
        }
        return success;
    }
}
