package cm.android.sdk.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class BaseAlarmReceiver extends BroadcastReceiver {

    private Context context;

    private AlarmTask alarmTask = new AlarmTask() {
        @Override
        protected Intent getIntent() {
            return BaseAlarmReceiver.this.getIntent();
        }

        @Override
        protected long getDelayAtMillis() {
            return BaseAlarmReceiver.this.getDelayAtMillis();
        }
    };

    public final void register(Context context) {
        IntentFilter intentFilter = createIntentFilter();
        register(context, intentFilter);
    }

    public final void register(Context context, IntentFilter intentFilter) {
        this.context = context;
        context.registerReceiver(this, intentFilter);

        startAlarm();
    }

    public final void unregister() {
        stopAlarm();

        context.unregisterReceiver(this);
        context = null;
    }

    public final void startAlarm() {
        alarmTask.start(context, getRequestCode());
    }

    public final void stopAlarm() {
        alarmTask.stop(context, getRequestCode());
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        if (getIntent().getAction().equals(intent.getAction())) {
            alarmTask.schedule(context, getRequestCode());
        }

        onHandleIntent(context, intent);
    }

    public IntentFilter createIntentFilter() {
        return new IntentFilter();
    }

    public int getRequestCode() {
        return 0;
    }

    protected abstract Intent getIntent();

    protected abstract long getDelayAtMillis();

    protected abstract void onHandleIntent(Context context, Intent intent);
}
