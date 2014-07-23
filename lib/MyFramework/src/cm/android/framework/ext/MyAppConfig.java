package cm.android.framework.ext;

import cm.android.framework.core.AppConfig;

/**
 */
public abstract class MyAppConfig extends AppConfig {

    /**
     * 初始化数据库表
     */
    protected abstract void initDatabase();
}
