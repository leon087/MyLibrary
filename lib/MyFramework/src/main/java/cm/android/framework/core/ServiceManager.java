package cm.android.framework.core;

import android.content.Context;
import android.os.IBinder;

public final class ServiceManager {

    private static final ApplicationImpl mApplication = new ApplicationImpl();

    static void appInit(Context context, AppConfig appConfig, IServiceManager serviceManager) {
        mApplication.appInit(context, appConfig, serviceManager);
    }

    public static void addService(String name, IBinder binder) {
        mApplication.addService(name, binder);
    }

    public static IBinder getService(String name) {
        return mApplication.getService(name);
    }

    public static boolean isBindService() {
        return mApplication.isBindService();
    }

    public static void start(InitListener initListener) {
        mApplication.start(initListener);
    }

    public static void stop() {
        mApplication.stop();
    }
}
