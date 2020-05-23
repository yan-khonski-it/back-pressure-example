package com.yk.training.backperssure;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class StackBlockingQueue<T> extends AbstractQueue<T> implements BlockingQueue<T> {

    private final LinkedBlockingDeque<T> linkedBlockingQueue;


    public StackBlockingQueue(final int maxSize) {
        this.linkedBlockingQueue = new LinkedBlockingDeque<>(maxSize);
    }

    @Override
    public Iterator<T> iterator() {
        return linkedBlockingQueue.iterator();
    }

    @Override
    public int size() {
        return linkedBlockingQueue.size();
    }

    @Override
    public void put(T t) throws InterruptedException {
        linkedBlockingQueue.putLast(t);
    }

    @Override
    public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
        return linkedBlockingQueue.offerLast(t, timeout, unit);
    }

    @Override
    public T take() throws InterruptedException {
        return linkedBlockingQueue.takeLast();
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return linkedBlockingQueue.pollLast(timeout, unit);
    }

    @Override
    public int remainingCapacity() {
        return linkedBlockingQueue.remainingCapacity();
    }

    @Override
    public int drainTo(Collection<? super T> c) {
        return linkedBlockingQueue.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super T> c, int maxElements) {
        return linkedBlockingQueue.drainTo(c, maxElements);
    }

    @Override
    public boolean offer(T t) {
        return linkedBlockingQueue.offerLast(t);
    }

    @Override
    public T poll() {
        return linkedBlockingQueue.pollLast();
    }

    @Override
    public T peek() {
        return linkedBlockingQueue.peekLast();
    }
}
