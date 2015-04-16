package cm.android.app.core;

import cm.android.app.test.TestContext;
import cm.android.app.test.server.TestManager;
import cm.android.framework.core.BinderFactory;
import cm.android.framework.core.global.GlobalData;
import cm.android.framework.ext.alarm.TimerManager;

public class MyManager {

    public static GlobalData getGlobalData() {
        return GlobalData.getInstance();
    }

    public static <T> T getData(String tag) {
        return GlobalData.getInstance().getData(tag);
    }

    public static <T> void putData(String tag, T value) {
        GlobalData.getInstance().putData(tag, value);
    }


    public static MainApp getApp() {
        return MainApp.getApp();
    }

    public static TestManager getTestManager() {
        TestManager test = BinderFactory.getProxy(TestContext.TEST, TestManager.class);
        return test;
    }

    public static TimerManager getTimerManager() {
        TimerManager test = BinderFactory.getProxy(TestContext.TIMER_TASK_SERVER,
                TimerManager.class);
        return test;
    }
}
