package cm.java.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleLock {
    private static final Logger logger = LoggerFactory.getLogger("thread");
    private final java.util.concurrent.locks.Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public SimpleLock() {
    }

    public void await() {
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    public void await(long time) {
        lock.lock();
        try {
            condition.await(time, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    public void signalAll() {
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
