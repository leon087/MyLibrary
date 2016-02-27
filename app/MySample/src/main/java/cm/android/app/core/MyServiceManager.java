package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import cm.android.app.test.TestContext;
import cm.android.app.test.server.TestManagerServer;
import cm.android.framework.core.BaseServiceManager;
import cm.android.framework.core.ServiceManager;
import cm.android.framework.ext.alarm.TimerServer;

public class MyServiceManager extends BaseServiceManager {

    private static final Logger logger = LoggerFactory.getLogger("MyServiceManager");

    private TestManagerServer testManager;

    private TimerServer timerServer;

    @Override
    protected void create(Context context) {
        testManager = new TestManagerServer();
        ServiceManager.addService(TestContext.TEST, testManager);

        timerServer = new TimerServer();
        timerServer.start(MyManager.getAppContext());

        ServiceManager.addService(TestContext.TIMER_TASK_SERVER, timerServer);
    }

    @Override
    protected void destroy() {
        timerServer.stop();
    }

    @Override
    protected void startService() {
        MainService.start();
    }

    @Override
    protected void stopService() {
        MainService.stop();
    }
}
