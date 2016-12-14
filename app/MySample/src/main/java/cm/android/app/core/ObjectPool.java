package cm.android.app.core;

import android.content.Context;

import cm.android.app.test.TestContext;
import cm.android.app.test.server.TestManager;
import cm.android.framework.client.core.Framework;
import cm.android.framework.ext.alarm.TimerManager;

public class ObjectPool {
    private static Context sAppContext;

    /**
     * Init.
     */
    public static final void init(Context appContext) {
        sAppContext = appContext.getApplicationContext();
    }


    public static Context getAppContext() {
        return sAppContext;
    }

    public static TestManager getTestManager() {
//        TestManager test = BinderFactory.getProxy(TestContext.TEST, TestManager.class);
        TestManager test = Framework.getBinderProxy(TestContext.TEST, TestManager.class);
        return test;
    }

    public static TimerManager getTimerManager() {
//        TimerManager test = BinderFactory.getProxy(TestContext.TIMER_TASK_SERVER,TimerManager.class);
        TimerManager test = Framework.getBinderProxy(TestContext.TIMER_TASK_SERVER, TimerManager.class);
        return test;
    }
}
