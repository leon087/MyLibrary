package cm.android.sdk;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class AlarmUtil {

    private AlarmUtil() {
    }

    public static void start(Context context, Intent broadcastIntent, int requestCode,
            long period) {
        PendingIntent sender = genPendingBroadcast(context, requestCode, broadcastIntent);
        long firstime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //循环发送广播
        am.setRepeating(AlarmManager.RTC_WAKEUP, firstime,
                period, sender);
    }

    public static void stop(Context context, Intent broadcastIntent, int requestCode) {
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
