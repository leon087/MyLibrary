package cm.java.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public abstract class Task<V> implements Runnable, Callable<V> {
    private volatile int state;
    //    private static final int IDLE = 0;
    private static final int NORMAL = 0;
    //    private static final int WORK = 1;
    private static final int CANCELLED = 2;

    private static final Logger logger = LoggerFactory.getLogger("thread");

    public Task() {
    }

    @Override
    public final void run() {
        reset();
        try {
            call();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public final void reset() {
        state = NORMAL;
    }

    public final void cancel() {
        state = CANCELLED;
    }

    public final boolean isCancelled() {
        return state == CANCELLED;
    }

//    @Override
//    public V call() throws Exception {
//        task.run();
//        return null;
//    }
}
