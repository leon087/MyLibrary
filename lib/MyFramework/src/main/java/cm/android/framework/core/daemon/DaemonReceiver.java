package cm.android.framework.core.daemon;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import cm.android.sdk.content.BaseBroadcastReceiver;

public class DaemonReceiver extends BaseBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, DaemonService.class));
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DaemonManager.ACTION_ALARM_WAKE_UP);
        return filter;
    }
}
