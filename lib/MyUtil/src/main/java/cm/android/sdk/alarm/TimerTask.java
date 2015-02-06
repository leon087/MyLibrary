package cm.android.sdk.alarm;

import android.content.Context;
import android.content.Intent;

public abstract class TimerTask {

    public final int requestCode = TimerTask.this.hashCode();

    public void start(Context context) {
        Intent intent = getIntent();
        int requestCode = getRequestCode();
        AlarmUtil.schedule(context, intent, requestCode, 0);
    }

    public void cancel(Context context) {
        Intent intent = getIntent();
        int requestCode = getRequestCode();
        AlarmUtil.cancel(context, intent, requestCode);
    }

    public void schedule(Context context) {
        Intent intent = getIntent();
        int requestCode = getRequestCode();
        AlarmUtil.schedule(context, intent, requestCode, getDelayAtMillis());
    }

    public int getRequestCode() {
        return 0;
    }

    protected abstract Intent getIntent();

    protected abstract long getDelayAtMillis();
}
