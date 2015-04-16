package cm.android.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.PowerManager;

public final class WakeLockUtil {

    private static final Logger logger = LoggerFactory.getLogger("WakeLockUtil");

    private static PowerManager.WakeLock newWakeLock(Context context, String tag) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                tag);

        return wakeLock;
    }

    public static PowerManager.WakeLock acquire(Context context, String tag) {
        logger.info("acquire:tag = " + tag);

        PowerManager.WakeLock wakeLock = newWakeLock(context, tag);
        wakeLock.acquire();

        return wakeLock;
    }

    public static PowerManager.WakeLock acquire(Context context, String tag, long timeout) {
        logger.info("acquire:tag = {},timeout = {}", tag, timeout);

        PowerManager.WakeLock wakeLock = newWakeLock(context, tag);
        wakeLock.acquire(timeout);

        return wakeLock;
    }

    public static void release(PowerManager.WakeLock wakeLock) {
        logger.info("release:isHeld = " + wakeLock.isHeld());

        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}
