package cm.android.sdk.alarm;

import android.content.Context;
import android.content.Intent;

public abstract class AlarmTask {

    public void start(Context context, int requestCode) {
        Intent intent = getIntent();
        AlarmUtil.schedule(context, intent, requestCode, 0);
    }

    public void stop(Context context, int requestCode) {
        Intent intent = getIntent();
        AlarmUtil.stop(context, intent, requestCode);
    }

    public void schedule(Context context, int requestCode) {
        Intent intent = getIntent();
        AlarmUtil.schedule(context, intent, requestCode, getDelayAtMillis());
    }

    protected abstract Intent getIntent();

    protected abstract long getDelayAtMillis();
}
