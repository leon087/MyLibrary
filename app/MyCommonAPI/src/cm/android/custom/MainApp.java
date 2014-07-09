package cm.android.custom;

import android.app.Activity;
import cm.android.framework.AppConfig;
import cm.android.framework.BaseApp;
import cm.android.framework.manager.BaseManager;

public class MainApp extends BaseApp {

	@Override
	public void onCreate() {
		super.onCreate();
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// cm.media.utils.Utils.initSo(this, "libCMallSDK.zip");
		// }
		// }).start();

		// HttpUtil.addHeader(header, value);

		MainApp.getApp().initApp();
	}

	@Override
	protected BaseManager initServiceManager() {
		return new MyServiceManager();
	}

	@Override
	protected AppConfig initConfig() {
		return new MainConfig();
	}

	@Override
	public synchronized void exitApp() {
		super.exitApp();
	}

	public void exitApp(Activity activity) {
		// Intent intent = MyIntent.getActivityIntent(this, InitActivity.class,
		// null, false);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// activity.setResult(InitActivity.CODE_EXIT, intent);
	}

	public void restartApp(Activity activity) {
		// ActivityStack.getInstance().finishAll();
		// Intent intent = MyIntent.getActivityIntent(this, InitActivity.class,
		// null, false);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// activity.startActivity(intent);
	}
}