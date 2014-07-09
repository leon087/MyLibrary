package cm.android.custom;

import cm.android.common.db.DatabaseConfig;
import cm.android.example.apis.common.dbdemo.TestBean;
import cm.android.framework.AppConfig;
import cm.android.framework.global.DirData;
import cm.android.util.util.MyLog.MyLogManager.Level;
import cm.android.util.util.MyLog.MyLogManager.LogMode;

public class MainConfig extends AppConfig {
	public static final String DOWNLOAD = "download/";

	@Override
	protected void initDatabase() {
		// 初始化数据库表
		DatabaseConfig.initDatabase(1, TestBean.class);
	}

	@Override
	protected void initDirName() {
		DirData.initDirName(DOWNLOAD);
	}

	@Override
	protected boolean initCrashReportFlag() {
		return false;
	}

	@Override
	protected Level initLogLevel() {
		return Level.DEBUG;
	}

	@Override
	protected LogMode initLogMode() {
		return LogMode.LOGCAT;
	}

	private static final class StoreRoot {
		/** 测试 */
		private static final String WONDER_TEST_OUT = "http://192.168.1.72:8080";
		/** 正式环境 */
		private static final String OFFICIAL = "http://";
	}

	/** IP */
	public static String SERVER_ROOT = StoreRoot.WONDER_TEST_OUT;

}
