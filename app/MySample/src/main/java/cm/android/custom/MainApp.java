package cm.android.custom;

import cm.android.framework.core.AppConfig;
import cm.android.framework.core.BaseApp;
import cm.android.framework.core.manager.BaseManager;

public class MainApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected BaseManager initServiceManager() {
        return new MyServiceManager();
    }

    @Override
    protected AppConfig initConfig() {
        return new MainConfig();
    }

}