package cm.android.sdk.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import cm.android.util.EnvironmentUtil;

public class AlarmUtil {

    private AlarmUtil() {
    }

    @Deprecated
    public static void startImmediately(Context context, Intent broadcastIntent, int requestCode,
            long period) {
        start(context, broadcastIntent, requestCode, 0, period);
    }

    @Deprecated
    public static void start(Context context, Intent broadcastIntent, int requestCode,
            long delayAtMillis, long period) {
        PendingIntent sender = genPendingBroadcast(context, requestCode, broadcastIntent);
        long currentTime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //循环发送广播
        am.cancel(sender);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, currentTime + delayAtMillis,
                period, sender);
    }

    public static void schedule(Context context, Intent broadcastIntent, int requestCode,
            long delayAtMillis) {
        PendingIntent sender = genPendingBroadcast(context, requestCode, broadcastIntent);
        long currentTime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        am.cancel(sender);
        setCompat(am, AlarmManager.ELAPSED_REALTIME_WAKEUP, currentTime + delayAtMillis, sender);
    }

    @TargetApi(19)
    private static void setCompat(AlarmManager am, int type, long triggerAtMillis,
            android.app.PendingIntent operation) {
        if (EnvironmentUtil.SdkUtil.hasKitkat()) {
            am.setExact(type, triggerAtMillis, operation);
        } else {
            am.set(type, triggerAtMillis, operation);
        }
    }

    public static void cancel(Context context, Intent broadcastIntent, int requestCode) {
        PendingIntent sender = genPendingBroadcast(context, requestCode, broadcastIntent);
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    private static PendingIntent genPendingBroadcast(Context context, int requestCode,
            Intent broadcastIntent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode,
                broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
