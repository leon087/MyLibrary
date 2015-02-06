package cm.android.sdk.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

    protected Context context;

    /**
     * 注册
     */
    public final void register(Context context) {
        IntentFilter intentFilter = createIntentFilter();
        register(context, intentFilter);
    }

    public final void register(Context context, IntentFilter intentFilter) {
        this.context = context;
        context.registerReceiver(this, intentFilter);
    }

    /**
     * 去注册
     */
    public final void unregister() {
        if (context == null) {
            return;
        }

        context.unregisterReceiver(this);
        context = null;
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

    /**
     * 创建IntentFilter
     */
    public IntentFilter createIntentFilter() {
        return new IntentFilter();
    }

}
