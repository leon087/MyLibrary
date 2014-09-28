package cm.android.framework.core;

import android.content.Context;
import android.content.Intent;

import cm.android.sdk.AlarmUtil;

public class DaemonManager {
    public static final String ACTION_ALARM_WAKE_UP = "cm.android.framework.intent.action.ALARM_WAKE_UP";

    private DaemonManager() {
    }

    private static final class Singleton {
        private static final DaemonManager INSTANCE = new DaemonManager();
    }

    public static DaemonManager getInstance() {
        return Singleton.INSTANCE;
    }

    public void startDaemon(Context context) {
        Intent alarmIntent = new Intent(context, CoreReceiver.class);
        alarmIntent.setAction(ACTION_ALARM_WAKE_UP);
        AlarmUtil.start(context, alarmIntent, 0, 60 * 1000);
    }

    public void stopDaemon(Context context) {
        Intent alarmIntent = new Intent(context, CoreReceiver.class);
        alarmIntent.setAction(ACTION_ALARM_WAKE_UP);
        AlarmUtil.stop(context, alarmIntent, 0);
    }
}
