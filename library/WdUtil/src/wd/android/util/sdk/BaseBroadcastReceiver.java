package wd.android.util.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * BroadcastReceiver包装类，增加了注册和去注册方法
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {
	protected Context context;

	public BaseBroadcastReceiver(Context context) {
		this.context = context;
	}

	/**
	 * 注册Intent
	 */
	public void registerReceiver() {
		IntentFilter intentFilter = createIntentFilter();
		context.registerReceiver(this, intentFilter);
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
	protected IntentFilter createIntentFilter() {
		return new IntentFilter();
	}

}
