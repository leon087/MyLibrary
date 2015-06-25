package cm.java.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public abstract class Task<V> implements Runnable, Callable<V> {

    private FutureTask task = null;

    public Task() {
        task = new FutureTask<V>(this);
//        task = new FutureTask<V>(this, null);
    }

    @Override
    public final void run() {
        task.run();
    }

    public final void cancel() {
        task.cancel(true);
    }

    public final boolean isCancelled() {
        return task.isCancelled();
    }

    public final boolean isDone() {
        return task.isDone();
    }

//    @Override
//    public V call() throws Exception {
//        task.run();
//        return null;
//    }
}
