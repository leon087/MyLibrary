package cm.android.framework.core.daemon;

import android.content.Context;
import android.content.Intent;

import cm.android.sdk.alarm.TimerTask;

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

    private TimerTask daemonTimerTask = new TimerTask() {
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

        @Override
        public int getRequestCode() {
            return hashCode();
        }
    };

    public void startDaemon(Context context) {
        daemonTimerTask.start(context);
    }

    public void stopDaemon(Context context) {
        daemonTimerTask.cancel(context);
    }

    public void schedule(Context context) {
        daemonTimerTask.schedule(context);
    }
}
