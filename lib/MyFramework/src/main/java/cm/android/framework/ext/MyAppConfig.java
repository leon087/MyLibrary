package cm.android.framework.ext;


import cm.android.framework.core.ServiceManager;

/**
 */
public abstract class MyAppConfig extends ServiceManager.AppConfig {

    /**
     * 初始化数据库表
     */
    protected abstract void initDatabase();
}
