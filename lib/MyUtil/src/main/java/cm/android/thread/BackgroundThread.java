package cm.android.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.sdk.MyHandler;
import cm.java.thread.SimpleLock;

public final class BackgroundThread {
    private static HandlerThread sThread;
    private static AsyncHandler sHandler;

    private BackgroundThread() {
    }

    public static synchronized void init() {
        sThread = new HandlerThread("BackgroundThread");
        sThread.start();
        sHandler = new AsyncHandler();
    }

    public static synchronized void deInit() {
        sHandler.exit();
        sThread.quit();
        sThread = null;
        sHandler = null;
    }

    public static synchronized Looper getLooper() {
        if (sThread == null) {
            return Looper.getMainLooper();
        }
        return sThread.getLooper();
    }

    public static synchronized Handler getHandler() {
        if (sThread == null) {
            return new Handler();
        }

        return sHandler.getHandler();
    }

    public static class AsyncHandler extends MyHandler {
        public AsyncHandler() {
            this(BackgroundThread.getLooper());
        }
        
        public AsyncHandler(Looper looper) {
            super(looper);
        }

        @Override
        public final void handleMessage(Message message) {
            handleFlag.set(true);
            try {
                //
                handleMessageImpl(message);
            } finally {
                handleFlag.set(false);
                lock.signalAll();
            }
        }

        protected void handleMessageImpl(Message message) {
        }

        private volatile AtomicBoolean handleFlag = new AtomicBoolean(false);
        private final SimpleLock lock = new SimpleLock();

        public void exit() {
            this.getHandler().removeCallbacksAndMessages(null);
            if (handleFlag.get()) {
                lock.await();
            }
        }
    }
}
