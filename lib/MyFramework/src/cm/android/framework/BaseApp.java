package cm.android.framework;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageInfo;
import cm.android.framework.manager.BaseManager;
import cm.android.applications.AppUtil;
import cm.android.util.ActivityStack;
import cm.android.util.EnvironmentInfo;
import cm.android.util.MyLog;

public abstract class BaseApp extends Application implements IApp {
	private static BaseApp sApp = null;
	private volatile boolean isInit = false;
	private BaseManager mServiceManager;

	@Override
	public void onCreate() {
		super.onCreate();
		sApp = this;
		initConfig().init();
		disableConnectionReuseIfNecessary();
		MyLog.initialize(this.getPackageName());
		PackageInfo packageInfo = AppUtil.getPackageInfo(
				this.getPackageManager(), this.getPackageName());
		MyLog.i("versionCode = " + packageInfo.versionCode + ",versionName = "
				+ packageInfo.versionName);
	}

	/**
	 * 禁用连接池
	 */
	private void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (!EnvironmentInfo.SdkUtil.hasFroyo()) {
			System.setProperty("http.keepAlive", "false");
		}
	}

	public static boolean isInit() {
		if (sApp == null) {
			return false;
		}
		return sApp.isInit;
	}

	/**
	 * 在假设Application不释放的情况下，进入app业务态，初始化资源
	 */
	@Override
	public synchronized void initApp() {
		MyLog.i("isInit = " + isInit);
		isInit = true;

		if (mServiceManager == null) {
			mServiceManager = initServiceManager();
			if (mServiceManager != null) {
				mServiceManager.create(sApp);
			}
		}
	}

	/**
	 * 退出app业务态，释放资源（注：结束进程只做辅助用）
	 */
	@Override
	public synchronized void exitApp() {
		MyLog.i("isInit = " + isInit);
		isInit = false;
		ActivityStack.getInstance().finishAll();

		if (null != mServiceManager) {
			mServiceManager.destroy();
			// 置null，以使得虚拟机主动回收该对象中资源
			mServiceManager = null;
			System.gc();
		}
		MyLog.release();

		// 退出，由于android不建议直接结束进程，故此处只做辅助用
		killSelf();
	}

	private void killSelf() {
		// 退出，由于android不建议直接结束进程，故此处只做辅助用
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		am.killBackgroundProcesses(getPackageName());
		// am.forceStopPackage(getPackageName());
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * 获取Application对象
	 * 
	 * @return BaseApp
	 */
	public synchronized static BaseApp getApp() {
		return sApp;
	}

	/**
	 * 初始化服务
	 * 
	 * @return
	 */
	protected abstract BaseManager initServiceManager();

	/**
	 * 初始化App运行配置
	 */
	protected abstract AppConfig initConfig();
}
