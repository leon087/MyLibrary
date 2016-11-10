package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import cm.android.app.test.TestContext;
import cm.android.app.test.server.TestManagerServer;
import cm.android.framework.client.core.Framework;
import cm.android.framework.ext.alarm.TimerServer;
import cm.android.framework.server.BaseBinderServer;

public class MyBinderServer extends BaseBinderServer {

    private static final Logger logger = LoggerFactory.getLogger("MyServiceManager");

    private TestManagerServer testManager;
    private Context context;

    private TimerServer timerServer;

    @Override
    protected void create(Context context) {
        android.util.Log.e("ggggggg", "ggggggg context = " + context.getApplicationContext());
        ObjectPool.init(context);
        this.context = context;
        testManager = new TestManagerServer();
        Framework.addService(TestContext.TEST, testManager);

        timerServer = new TimerServer();
        timerServer.start(context);

        Framework.addService(TestContext.TIMER_TASK_SERVER, timerServer);

//        AppContext.register(TestContext.TEST, testManager);
    }

    @Override
    protected void destroy() {
        Framework.clearService();
        timerServer.stop();
        this.context = null;
    }

    @Override
    protected void startService() {
        MainService.start(context);
    }

    @Override
    protected void stopService() {
        MainService.stop(context);
    }
}
