package cm.android.app.core;

import cm.android.app.test.TestManager;
import cm.android.framework.core.BinderFactory;
import cm.android.framework.core.global.GlobalData;

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

    public static TestManager.TestManagerProxy getTestManager() {
        TestManager.TestManagerProxy test = BinderFactory.getProxy("Test",
                TestManager.TestManagerProxy.class);
        return test;
    }
}
