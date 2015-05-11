package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import cm.android.app.test.TestContext;
import cm.android.app.test.server.TestManagerServer;
import cm.android.framework.core.IServiceManager;
import cm.android.framework.core.ServiceManager;
import cm.android.framework.ext.alarm.TimerServer;
import cm.android.util.SystemUtil;

public class MyServiceManager implements IServiceManager {

    private static final Logger logger = LoggerFactory.getLogger("MyServiceManager");

    private TestManagerServer testManager;

    private TimerServer timerServer;

    @Override
    public void onCreate(Context context) {
        logger.error("ggggg initService:onCreate:processName = " + SystemUtil
                .getCurProcessName(MyManager.getApp()));
        testManager = new TestManagerServer();
        ServiceManager.addService(TestContext.TEST, testManager);

        timerServer = new TimerServer();
        timerServer.start(MyManager.getApp());

        ServiceManager.addService(TestContext.TIMER_TASK_SERVER, timerServer);
    }

    @Override
    public void onDestroy() {
        timerServer.stop();

        logger.error("ggggg onDestroy");
    }
}
