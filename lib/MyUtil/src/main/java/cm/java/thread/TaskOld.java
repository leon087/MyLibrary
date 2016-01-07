package cm.java.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 只支持执行一次
 */
public abstract class TaskOld<V> implements Runnable, Callable<V> {

    private FutureTask task = null;

    public TaskOld() {
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
