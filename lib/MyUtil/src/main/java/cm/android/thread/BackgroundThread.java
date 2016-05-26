package cm.android.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.sdk.WeakHandler;
import cm.java.thread.SimpleLock;

public final class BackgroundThread {
    private static final Logger logger = LoggerFactory.getLogger("BackgroundThread");
    private static HandlerThread sThread;
    private static AsyncHandler sHandler;

    private BackgroundThread() {
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s:%s:%d";
        tag = String.format(Locale.getDefault(), tag, caller.getFileName(), caller.getMethodName(),
                caller.getLineNumber());
        return tag;
    }

    public static synchronized void init() {
//        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        sThread = new HandlerThread("BackgroundThread", Process.THREAD_PRIORITY_BACKGROUND);
        sThread.start();
        sHandler = new AsyncHandler();
        logger.info("init");
    }

    public static synchronized void deInit() {
        logger.info("deInit");
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

        return sHandler.get();
    }

    public static class AsyncHandler extends WeakHandler {
        public AsyncHandler() {
            this(BackgroundThread.getLooper());
        }

        public AsyncHandler(Looper looper) {
            super(looper);
        }

        @Override
        public final void handleMessage(Message message) {
            logger.info("{}:handleMessage:begin", this);
            handleFlag.set(true);
            try {
                //
                handleMessageImpl(message);
            } finally {
                handleFlag.set(false);
                lock.signalAll();
                logger.info("{}:handleMessage:end", this);
            }
        }

        protected void handleMessageImpl(Message message) {
        }

        private volatile AtomicBoolean handleFlag = new AtomicBoolean(false);
        private final SimpleLock lock = new SimpleLock();

        public void exit() {
            this.get().removeCallbacksAndMessages(null);
            logger.info("{}:exit:begin", this);
            while (handleFlag.get()) {
                lock.await();
            }
            logger.info("{}:exit:end", this);
        }
    }
}
