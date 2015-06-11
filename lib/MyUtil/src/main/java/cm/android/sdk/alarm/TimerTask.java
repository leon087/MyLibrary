package cm.android.sdk.alarm;

import android.content.Context;
import android.content.Intent;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TimerTask {

    public final int requestCode = TimerTask.this.hashCode();

    private final AtomicBoolean startFlag = new AtomicBoolean(false);

    public void start(Context context) {
        start(context, 0);
    }

    public void start(Context context, long delayAtMillis) {
        if (startFlag.compareAndSet(false, true)) {
            Intent intent = getIntent(context);
            intent.setPackage(context.getPackageName());
            int requestCode = getRequestCode();
            AlarmUtil.schedule(context, intent, requestCode, delayAtMillis);
        }
    }

    public void cancel(Context context) {
        if (startFlag.compareAndSet(true, false)) {
            Intent intent = getIntent(context);
            intent.setPackage(context.getPackageName());
            int requestCode = getRequestCode();
            AlarmUtil.cancel(context, intent, requestCode);
        }
    }

    public void schedule(Context context) {
        if (!startFlag.get()) {
            return;
        }

        Intent intent = getIntent(context);
        intent.setPackage(context.getPackageName());
        int requestCode = getRequestCode();
        AlarmUtil.schedule(context, intent, requestCode, getDelayAtMillis());
    }

    public int getRequestCode() {
        return 0;
    }

    protected abstract Intent getIntent(Context context);

    protected abstract long getDelayAtMillis();
}
