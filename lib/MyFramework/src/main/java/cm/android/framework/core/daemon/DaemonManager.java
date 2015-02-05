package cm.android.framework.core.daemon;

import android.content.Context;
import android.content.Intent;

import cm.android.sdk.alarm.AlarmTask;

public class DaemonManager {

    public static final String ACTION_ALARM_WAKE_UP
            = "cm.android.framework.intent.action.ALARM_WAKE_UP";

    private static final long DELAY = 20 * 60 * 1000;

    private DaemonManager() {
    }

    private static final class Singleton {

        private static final DaemonManager INSTANCE = new DaemonManager();
    }

    public static DaemonManager getInstance() {
        return Singleton.INSTANCE;
    }

    private AlarmTask daemonAlarmTask = new AlarmTask() {
        @Override
        protected Intent getIntent() {
            Intent intent = new Intent();
            intent.setAction(ACTION_ALARM_WAKE_UP);
            return intent;
        }

        @Override
        protected long getDelayAtMillis() {
            return DELAY;
        }
    };

    public void startDaemon(Context context) {
        daemonAlarmTask.start(context, 0);
    }

    public void stopDaemon(Context context) {
        daemonAlarmTask.stop(context, 0);
    }

    public void schedule(Context context) {
        daemonAlarmTask.schedule(context, 0);
    }
}
