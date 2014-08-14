package cm.android.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * BroadcastReceiver包装类，增加了注册和去注册方法
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {
    protected Context context;

    /**
     * 注册Intent
     */
    public void registerReceiver(Context context) {
        IntentFilter intentFilter = createIntentFilter();
        context.registerReceiver(this, intentFilter);
        this.context = context;
    }

    public void registerReceiver(IntentFilter intentFilter) {
        context.registerReceiver(this, intentFilter);
    }

    /**
     * 去注册
     */
    public void unRegisterReceiver() {
        context.unregisterReceiver(this);
    }

    /**
     * 创建IntentFilter
     *
     * @return
     */
    public IntentFilter createIntentFilter() {
        return new IntentFilter();
    }

}
