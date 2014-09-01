package cm.android.thread;

public class Sync<T> extends Object {
    private volatile T result = null;
    private volatile boolean completed = false;

    public void set(T res) {
        // log.d("sync.set() called from " + Thread.currentThread().getName());
        result = res;
        completed = true;
        synchronized (this) {
            notify();
        }
        // log.d("sync.set() returned from notify "
        // + Thread.currentThread().getName());
    }

    public T get(long time) {
        // log.d("sync.get() called from " + Thread.currentThread().getName());
        while (!completed) {
            try {
                // log.d("sync.get() before wait "
                // + Thread.currentThread().getName());
                synchronized (this) {
                    if (!completed)
                        wait(time);
                }
                // log.d("sync.get() after wait wait "
                // + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                // log.d("sync.get() exception", e);
                // ignore
            } catch (Exception e) {
                // log.d("sync.get() exception", e);
                // ignore
            }
        }
        // log.d("sync.get() returning " + Thread.currentThread().getName());
        return result;
    }
}
