package cm.android.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Looper;

import java.util.Locale;

public final class BackgroundThread {
    private static final Logger logger = LoggerFactory.getLogger("BackgroundThread");
    private static Background background = new Background();

    private BackgroundThread() {
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s:%s:%d";
        tag = String.format(Locale.getDefault(), tag, caller.getFileName(), caller.getMethodName(),
                caller.getLineNumber());
        return tag;
    }

    public static synchronized void init() {
        logger.info("init");
        background.init();
    }

    public static synchronized void deInit() {
        logger.info("deInit");
        background.deInit();
    }

    public static synchronized Looper getLooper() {
        return background.getLooper();
    }

    public static synchronized android.os.Handler getHandler() {
        return background.getHandler();
    }

    public static class Handler extends AsyncHandler {
        public Handler() {
            super(background.getLooper());
        }
    }
}