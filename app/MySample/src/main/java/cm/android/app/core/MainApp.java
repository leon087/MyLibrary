package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import cm.android.framework.core.BaseApp;
import cm.android.framework.core.IServiceManager;
import cm.android.framework.core.ServiceManager;

public class MainApp extends BaseApp {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private static MainApp sMainApp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        android.support.multidex.MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.error("ggg application onCreate");
        sMainApp = this;
    }

    public static MainApp getApp() {
        return sMainApp;
    }

    @Override
    public ServiceManager.AppConfig initConfig() {
        return new MainConfig();
    }

    @Override
    protected Class<? extends IServiceManager> initServiceManager() {
        return MyServiceManager.class;
    }
}