package cm.android.framework;

/**
 */
public abstract class AppConfig implements IAppConfig {

    /**
     * 初始化
     */
    protected void init() {
        initLog();
        initWorkDir();
    }
}
