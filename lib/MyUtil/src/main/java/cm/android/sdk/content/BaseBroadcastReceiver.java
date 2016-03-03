package cm.android.sdk.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

    public void register(Context context) {
        IntentFilter intentFilter = createIntentFilter();
        register(context, intentFilter);
    }

    public void register(Context context, IntentFilter intentFilter) {
        register(context, intentFilter, getPermission());
    }

    public void register(Context context, IntentFilter intentFilter,
                         String broadcastPermission) {
        context.registerReceiver(this, intentFilter, broadcastPermission, null);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    public void registerLocal(Context context, IntentFilter intentFilter) {
        LocalBroadcastManager.getInstance(context).registerReceiver(this, intentFilter);
    }

    public void registerLocal(Context context) {
        IntentFilter intentFilter = createIntentFilter();
        registerLocal(context, intentFilter);
    }

    public void unregisterLocal(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

    public IntentFilter createIntentFilter() {
        return new IntentFilter();
    }

    public String getPermission() {
        return null;
    }

}
