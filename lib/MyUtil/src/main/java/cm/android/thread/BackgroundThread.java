package cm.android.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import java.util.Locale;

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
        sHandler = new BackgroundThread.Handler();
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

    public static synchronized android.os.Handler getHandler() {
        if (sThread == null) {
            return new android.os.Handler();
        }

        return sHandler.get();
    }

    public static class Handler extends AsyncHandler {
        public Handler() {
            super(BackgroundThread.getLooper());
        }
    }
}