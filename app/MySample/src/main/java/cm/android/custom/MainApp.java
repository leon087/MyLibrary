package cm.android.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.RemoteException;

import cm.android.app.TestService1;
import cm.android.app.sample.ITestManager;
import cm.android.framework.core.AppConfig;
import cm.android.framework.core.BaseApp;
import cm.android.framework.core.IServiceManager;
import cm.android.framework.core.ServiceManager;

public class MainApp extends BaseApp {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    @Override
    public void onCreate() {
        super.onCreate();
        logger.error("ggg application onCreate");

        startService(new Intent(this, TestService1.class));
    }

    @Override
    public AppConfig initConfig() {
        return new MainConfig();
    }

    @Override
    public cm.android.framework.core.IServiceManager initService() {
        return new IServiceManager.Stub() {
            @Override
            public void onCreate() throws RemoteException {
                logger.error("ggggg initService:onCreate");
                TestManager testManager = new TestManager();
                ServiceManager.addService("Test", testManager);
            }

            @Override
            public void onDestroy() throws RemoteException {

            }

            ;
        };
    }

    public static class TestManager extends ITestManager.Stub {

        private int count;

        @Override
        public void count() throws RemoteException {
            logger.error("ggg count = " + count);
            count++;
        }
    }
}