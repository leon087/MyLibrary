package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.RemoteException;

import cm.android.app.test.TestContext;
import cm.android.app.test.TestService1;
import cm.android.app.test.server.TestManagerServer;
import cm.android.app.test.server.TimerTaskServer;
import cm.android.framework.core.BaseApp;
import cm.android.framework.core.IServiceManager;
import cm.android.framework.core.ServiceManager;
import cm.android.util.SystemUtil;

public class MainApp extends BaseApp {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    @Override
    public void onCreate() {
        super.onCreate();
        logger.error("ggg application onCreate");

        ServiceManager.start(new ServiceManager.InitListener() {
            @Override
            public void initSucceed() {
                logger.error("ggg testService1 initSucceed");
                startService(new Intent(MainApp.this, TestService1.class));
            }
        });
    }


    @Override
    public ServiceManager.AppConfig initConfig() {
        return new MainConfig();
    }

    @Override
    public cm.android.framework.core.IServiceManager initService() {
        return new IServiceManager.Stub() {
            @Override
            public void onCreate() throws RemoteException {
                logger.error("ggggg initService:onCreate:processName = " + SystemUtil
                        .getCurProcessName(getApplicationContext()));
                TestManagerServer testManager = new TestManagerServer();
                ServiceManager.addService(TestContext.TEST, testManager);

                TimerTaskServer timerTaskServer = new TimerTaskServer();
                timerTaskServer.start();

                ServiceManager.addService(TestContext.TIMER_TASK_SERVER, timerTaskServer);
            }

            @Override
            public void onDestroy() throws RemoteException {

            }

            ;
        };
    }
}