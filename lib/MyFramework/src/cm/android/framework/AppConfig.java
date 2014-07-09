package cm.android.framework;

import cm.android.util.CrashHandler;
import cm.android.util.MyLog;
import cm.android.util.MyLog.MyLogManager.Level;

public abstract class AppConfig {

	/**
	 * crash报告是否记录到sdcard，默认为false
	 * 
	 * @param crashReportFlag
	 *            false表示不记录
	 */
	private static void setCrashReportFlag(boolean crashReportFlag) {
		if (!crashReportFlag) {
			return;
		}

		CrashHandler crashHandler = CrashHandler.getInstance();
		// 注册crashHandler
		crashHandler.init(BaseApp.getApp().getApplicationContext());
		// 发送以前没发送的报告(可选)
		crashHandler.sendPreviousReportsToServer();
	}

	/**
	 * 初始化
	 */
	void init() {
		setLog();
		setCrashReportFlag();
		initDatabase();
		initDirName();
	}

	private void setLog() {
		MyLog.setLogLevel(initLogLevel());
		MyLog.setLogMode(initLogMode());
	}

	private void setCrashReportFlag() {
		setCrashReportFlag(initCrashReportFlag());
	}

	/**
	 * 初始化数据库表
	 */
	protected abstract void initDatabase();

	/**
	 * 初始化目录
	 */
	protected abstract void initDirName();

	/**
	 * 设置是否需要记录CrashReport
	 * 
	 * @return false表示不记录
	 */
	protected abstract boolean initCrashReportFlag();

	/**
	 * 设置log记录级别
	 * 
	 * @return {@link Level#DEBUG}, {@link Level#INFO}, {@link Level#ERROR},
	 *         {@link Level#LEVEL_MAX}
	 */
	protected abstract MyLog.MyLogManager.Level initLogLevel();

	protected abstract MyLog.MyLogManager.LogMode initLogMode();
}
