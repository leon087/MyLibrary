package cm.android.framework.core;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import cm.android.sdk.BaseBroadcastReceiver;

public class CoreReceiver extends BaseBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, CoreService.class));
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        return filter;
    }
}
