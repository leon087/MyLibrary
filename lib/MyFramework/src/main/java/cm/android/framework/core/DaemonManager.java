package cm.android.framework.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class DaemonManager {
    public static final String ACTION_ALARM_WAKE_UP = "cm.android.framework.ACTION_ALARM_WAKE_UP";

    private DaemonManager() {
    }

    private static final class Singleton {
        private static final DaemonManager INSTANCE = new DaemonManager();
    }

    public static DaemonManager getInstance() {
        return Singleton.INSTANCE;
    }

    private PendingIntent pendingIntent = null;

    public void startDaemon(Context context) {
        PendingIntent sender = genPendingIntent(context);
        long firstime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //循环发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
                60 * 1000, sender);
    }

    public void stopDaemon(Context context) {
        PendingIntent sender = genPendingIntent(context);
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    private PendingIntent genPendingIntent(Context context) {
        if (pendingIntent == null) {
            Intent alarmIntent = new Intent(context, CoreReceiver.class);
            alarmIntent.setAction(ACTION_ALARM_WAKE_UP);
            pendingIntent = PendingIntent.getBroadcast(context, 0,
                    alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return pendingIntent;
    }
}
