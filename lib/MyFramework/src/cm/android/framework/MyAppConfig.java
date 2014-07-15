package cm.android.framework;

import cm.android.util.MyLog;

/**
 */
public abstract class MyAppConfig extends AppConfig {

    /**
     * 初始化数据库表
     */
    protected abstract void initDatabase();

    /**
     * 设置log记录级别
     *
     * @return {@link cm.android.util.MyLog.MyLogManager.Level#DEBUG}, {@link cm.android.util.MyLog.MyLogManager.Level#INFO}, {@link cm.android.util.MyLog.MyLogManager.Level#ERROR},
     */
    protected abstract MyLog.MyLogManager.Level initLogLevel();

    protected abstract MyLog.MyLogManager.LogMode initLogMode();

}
