package cm.android.framework.core;

import android.app.Application;

import cm.android.util.EnvironmentUtil;

public abstract class BaseApp extends Application implements IApp {

    @Override
    public void onCreate() {
        super.onCreate();

        disableConnectionReuseIfNecessary();

        AppConfig appConfig = initConfig();
        IServiceManager serviceManager = initService();
        ServiceManager.appInit(this, appConfig, serviceManager);
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

}