package cm.android.framework.core;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;

@Deprecated
public final class ServiceManager {

    public static final String ACTION_BIND_SUCCEED
            = "cm.android.framework.intent.action.BIND_SUCCEED";

    public static interface InitListener {

        void initSucceed();
    }

    public static interface IAppConfig {

        void initEnvironment(Context context);

        void initLog(Context context);

        void initWorkDir(Context context);
    }

    public static abstract class AppConfig implements IAppConfig {

        /**
         * 初始化
         */
        public void init(Context context) {
            initEnvironment(context);
            initWorkDir(context);
            initLog(context);
        }
    }

    private static final ApplicationImpl APPLICATION = new ApplicationImpl();

    static void appInit(Context context, AppConfig appConfig, Class<? extends IServiceManager> serviceClass) {
        APPLICATION.appInit(context, appConfig, serviceClass);
    }
//    static void appInit(Context context, AppConfig appConfig,
//            Class<? extends AbstractCoreService> clazz) {
//        APPLICATION.appInit(context, appConfig, clazz);
//    }

    public static void addService(String name, IBinder binder) {
        APPLICATION.addService(name, binder);
    }

    public static IBinder getService(String name) {
        return APPLICATION.getService(name);
    }

    public static boolean isSystemReady() {
        return APPLICATION.isSystemReady();
    }

    public static void start(InitListener initListener) {
        APPLICATION.start(initListener);
    }

    public static void stop() {
        APPLICATION.stop();
    }

    public static boolean isStarted() {
        return APPLICATION.isStarted();
    }

    public static void restoreService(Context context, String processName) {
        CoreReceiver.restore(context, processName);
    }

    public static void handleAction(String action, Bundle bundle) {
        APPLICATION.handleAction(action, bundle);
    }

}
