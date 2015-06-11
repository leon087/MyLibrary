package cm.android.framework.core.daemon;

import android.content.Context;
import android.content.Intent;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.sdk.alarm.TimerTask;

public class DaemonManager {

    public static final String ACTION_ALARM_WAKE_UP
            = "cm.android.framework.intent.action.ALARM_WAKE_UP";

    private static final long DELAY = 1 * 60 * 60 * 1000;

    private static final AtomicBoolean startFlag = new AtomicBoolean(false);

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
        protected Intent getIntent(Context context) {
            Intent intent = new Intent();
            intent.setPackage(context.getPackageName());
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
        if (startFlag.compareAndSet(false, true)) {
            daemonTimerTask.start(context);
        }
    }

    public void stopDaemon(Context context) {
        if (startFlag.compareAndSet(true, false)) {
            daemonTimerTask.cancel(context);
        }
    }

    public void schedule(Context context) {
        if (!startFlag.get()) {
            return;
        }

        daemonTimerTask.schedule(context);
    }
}
