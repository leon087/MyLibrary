package cm.android.framework.server.daemon;

import android.content.Context;
import android.content.Intent;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.sdk.alarm.TimerTask;

public class DaemonManager {

    public static final String ACTION_ALARM_WAKE_UP
            = "cm.android.framework.intent.action.ALARM_WAKE_UP";

    private static final long DELAY = 1 * 60 * 60 * 1000L;

    private final AtomicBoolean startFlag = new AtomicBoolean(false);

    private DaemonManager() {
    }

    private static final cm.java.util.Singleton<DaemonManager> singleton = new cm.java.util.Singleton<DaemonManager>() {
        @Override
        protected DaemonManager create() {
            return new DaemonManager();
        }
    };

    public static DaemonManager getInstance() {
        return singleton.get();
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
            daemonTimerTask.start(context, DELAY);
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
