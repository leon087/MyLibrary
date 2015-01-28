package cm.android.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.RemoteException;

import cm.android.app.MainService;
import cm.android.framework.core.AppConfig;
import cm.android.framework.core.BaseApp;
import cm.android.framework.core.IManager;
import cm.android.framework.core.IServiceManager;
import cm.android.framework.core.ServiceManager;

public class MainApp extends BaseApp {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    @Override
    public void onCreate() {
        super.onCreate();
        logger.error("ggg application onCreate");

        startService(new Intent(this, MainService.class));
    }

    @Override
    protected AppConfig initConfig() {
        return new MainConfig();
    }

    @Override
    protected cm.android.framework.core.IServiceManager initService() {
        return new IServiceManager.Stub() {
            @Override
            public void onCreate() throws RemoteException {
                ServiceManager.addService("Test", new TestManager());
            }

            @Override
            public void onDestroy() throws RemoteException {

            }

            ;
        };
    }

    public static class TestManager extends IManager.Stub {

        @Override
        public void initialize() throws RemoteException {
            logger.error("ggg initialize");
        }

        @Override
        public void release() throws RemoteException {

        }
    }
}