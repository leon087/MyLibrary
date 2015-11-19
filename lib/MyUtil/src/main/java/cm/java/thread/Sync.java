package cm.java.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sync<T> extends Object {

    private static final Logger logger = LoggerFactory.getLogger("Sync");

    private volatile T result = null;

    private volatile boolean completed = false;

    public void reset() {
        completed = false;
        result = null;
    }

    public void set(T res) {
        logger.debug("sync.set() called from {}", Thread.currentThread().getName());
        result = res;
        completed = true;
        synchronized (this) {
            notifyAll();
        }
        logger.debug("sync.set() returned from notify {}", Thread.currentThread().getName());
    }

    public T get(long time) {
        logger.debug("sync.get() called from {}", Thread.currentThread().getName());
        while (!completed) {
            try {
                logger.debug("sync.get() before wait {}", Thread.currentThread().getName());
                synchronized (this) {
                    if (!completed) {
                        waitInternal(time);
                    }
                }
                logger.debug("sync.get() after wait wait {}", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                // log.d("sync.get() exception", e);
                // ignore
            } catch (Exception e) {
                // log.d("sync.get() exception", e);
                // ignore
            }
        }
        logger.debug("sync.get() returning {}", Thread.currentThread().getName());
        return result;
    }

    private void waitInternal(long time) throws InterruptedException {
        if (time <= 0) {
            wait();
        } else {
            wait(time);
        }
    }
}
