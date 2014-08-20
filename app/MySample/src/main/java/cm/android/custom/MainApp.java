package cm.android.custom;

import cm.android.framework.core.AppConfig;
import cm.android.framework.core.BaseApp;
import cm.android.framework.core.manager.BaseManager;

public class MainApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
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
}