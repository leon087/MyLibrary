package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;

import cm.android.app.test.TestService1;
import cm.android.framework.core.BaseApp;
import cm.android.framework.core.ServiceManager;

public class MainApp extends BaseApp {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private static MainApp sMainApp;

    @Override
    public void onCreate() {
        super.onCreate();
        logger.error("ggg application onCreate");
        sMainApp = this;

        ServiceManager.start(new ServiceManager.InitListener() {
            @Override
            public void initSucceed() {
                logger.error("ggg testService1 initSucceed");
                startService(new Intent(MainApp.this, TestService1.class));
            }
        });
    }

    public static MainApp getApp() {
        return sMainApp;
    }

    @Override
    public ServiceManager.AppConfig initConfig() {
        return new MainConfig();
    }

    @Override
    public cm.android.framework.core.IServiceManager initService() {
        return new MyServiceManager();
    }
}