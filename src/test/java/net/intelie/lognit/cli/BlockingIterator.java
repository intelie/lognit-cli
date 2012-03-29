package net.intelie.lognit.cli;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class BlockingIterator<T> implements Iterator<T> {
    private final Iterator<T> underlying;
    private final Semaphore iAmGrounded, calledNext, calledRemove;

    public BlockingIterator(Iterator<T> underlying) {
        this.underlying = underlying;
        this.iAmGrounded = new Semaphore(0);
        this.calledNext = new Semaphore(0);
        this.calledRemove = new Semaphore(0);
    }
    
    @Override
    public boolean hasNext() {
        return underlying.hasNext();
    }

    public void release() {
        iAmGrounded.release();
    }

    public void releaseAndWaitNext() {
        release();
        waitNext();
    }

    public void releaseAndWaitRemove() {
        release();
        waitRemove();
    }

    public void waitNext() {
        calledNext.acquireUninterruptibly();
    }

    public void waitRemove() {
        calledRemove.acquireUninterruptibly();
    }

    @Override
    public T next() {
        calledNext.release();
        iAmGrounded.acquireUninterruptibly();
        return underlying.next();
    }

    @Override
    public void remove() {
        calledRemove.release();
        iAmGrounded.acquireUninterruptibly();
        underlying.remove();
    }
}
