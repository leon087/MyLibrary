package cm.android.sdk.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class BaseAlarmReceiver extends BroadcastReceiver {

    private Context context;

    private TimerTask timerTask = new TimerTask() {
        @Override
        protected Intent getIntent() {
            return BaseAlarmReceiver.this.getIntent();
        }

        @Override
        protected long getDelayAtMillis() {
            return BaseAlarmReceiver.this.getDelayAtMillis();
        }

        @Override
        public int getRequestCode() {
            return BaseAlarmReceiver.this.getRequestCode();
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
        if (context == null) {
            return;
        }

        stopAlarm();

        context.unregisterReceiver(this);
        context = null;
    }

    public final void startAlarm() {
        timerTask.start(context);
    }

    public final void stopAlarm() {
        timerTask.cancel(context);
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        if (getIntent().getAction().equals(intent.getAction())) {
            timerTask.schedule(context);
        }

        onHandleIntent(context, intent);
    }

    public IntentFilter createIntentFilter() {
        return new IntentFilter();
    }

    protected int getRequestCode() {
        return 0;
    }

    protected abstract Intent getIntent();

    protected abstract long getDelayAtMillis();

    protected abstract void onHandleIntent(Context context, Intent intent);
}
