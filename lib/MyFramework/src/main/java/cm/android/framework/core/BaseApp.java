package cm.android.framework.core;

import android.app.Application;
import android.content.Context;

import cm.android.util.EnvironmentUtil;

public abstract class BaseApp extends Application implements IApp {

    private static BaseApp sApp;

    public static BaseApp getApp() {
        return sApp;
    }

    private static void set(BaseApp app) {
        sApp = app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        sApp = this;
        set(this);

        disableConnectionReuseIfNecessary();

        ServiceManager.AppConfig appConfig = initConfig();
        ServiceManager.appInit(this, appConfig, initServiceManager());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 禁用连接池
     */
    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (!EnvironmentUtil.SdkUtil.hasFroyo()) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    protected abstract Class<? extends IServiceManager> initServiceManager();
}