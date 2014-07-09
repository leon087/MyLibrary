package cm.android.util.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import cm.android.util.util.MyLog;

/**
 * sdcard监听类
 */
public class ExternalStorageReceiver extends BaseBroadcastReceiver {
	private ExternalStorageListener externalStorageListener;

	/**
	 * 初始化
	 */
	public ExternalStorageReceiver(Context context,
			ExternalStorageListener listener) {
		super(context);
		externalStorageListener = listener;
		registerReceiver();
	}

	/**
	 * 释放资源
	 */
	public void release() {
		unRegisterReceiver();
	}

	@Override
	protected IntentFilter createIntentFilter() {
		IntentFilter intentFilter = super.createIntentFilter();
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		intentFilter.addDataScheme("file");
		return intentFilter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		MyLog.i("intent = " + intent);
		String action = intent.getAction();

		if (Intent.ACTION_MEDIA_MOUNTED.equals(action)
				|| Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)
				|| Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
			// SD卡成功挂载
			externalStorageListener.onMediaMounted();
		} else if (Intent.ACTION_MEDIA_REMOVED.equals(action)
				|| Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
				|| Intent.ACTION_MEDIA_BAD_REMOVAL.equals(action)) {
			// SD卡挂载失败
			externalStorageListener.onMediaRemoved();
		}
	}

	/**
	 * sdcard监听接口
	 */
	public interface ExternalStorageListener {
		/**
		 * sdcard已挂载
		 */
		void onMediaMounted();

		/**
		 * sdcard已被移除
		 */
		void onMediaRemoved();
	}
}