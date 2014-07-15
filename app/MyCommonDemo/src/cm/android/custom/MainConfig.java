package cm.android.custom;

import cm.android.framework.MyAppConfig;
import cm.android.framework.global.DirData;
import cm.android.util.MyLog;
import cm.android.util.MyLog.MyLogManager.Level;
import cm.android.util.MyLog.MyLogManager.LogMode;

public class MainConfig extends MyAppConfig {
    public static final String DOWNLOAD = "download/";

    @Override
    protected void initDatabase() {
        // 初始化数据库版本
        // DatabaseConfig.initVersion(1);

        // 初始化数据库表
        // DatabaseConfig.initTable(ApkBean.class);
    }

    @Override
    public void initWorkDir() {
        DirData.initDirName(DOWNLOAD);
    }

    @Override
    public void initLog() {
        MyLog.setLogMode(initLogMode());
        MyLog.setLogLevel(initLogLevel());
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
        /**
         * 测试
         */
        private static final String WONDER_TEST_OUT = "http://192.168.1.72:8080";
        /**
         * 正式环境
         */
        private static final String OFFICIAL = "http://";
    }

    /**
     * IP
     */
    public static String SERVER_ROOT = StoreRoot.WONDER_TEST_OUT;

}
