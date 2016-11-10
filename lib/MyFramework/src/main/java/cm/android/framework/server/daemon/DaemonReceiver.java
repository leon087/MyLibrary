package cm.android.framework.server.daemon;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import cm.android.sdk.content.BaseBroadcastReceiver;

public class DaemonReceiver extends BaseBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DaemonManager.getInstance().schedule(context);

        DaemonService.start(context);
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DaemonManager.ACTION_ALARM_WAKE_UP);
        return filter;
    }

}
