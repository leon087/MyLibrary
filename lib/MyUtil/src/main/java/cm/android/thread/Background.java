package cm.android.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import java.util.Locale;

public final class Background {
    private static final Logger logger = LoggerFactory.getLogger("Background");
    private HandlerThread thread;
    private AsyncHandler handler;

    public Background() {
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s:%s:%d";
        tag = String.format(Locale.getDefault(), tag, caller.getFileName(), caller.getMethodName(),
                caller.getLineNumber());
        return tag;
    }

    public synchronized void init() {
//        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        thread = new HandlerThread("Background", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        handler = new AsyncHandler(thread.getLooper());
        logger.info("init");
    }

    public synchronized void deInit() {
        logger.info("deInit");
        handler.exit();
        thread.quit();
        thread = null;
        handler = null;
    }

    public synchronized Looper getLooper() {
        if (thread == null) {
            logger.error("sThread = null:return Looper.getMainLooper()");
            return Looper.getMainLooper();
        }
        return thread.getLooper();
    }

    public synchronized android.os.Handler getHandler() {
        if (thread == null) {
            return new android.os.Handler();
        }

        return handler.get();
    }
}