package cm.android.framework.core;

import android.content.Context;
import android.os.IBinder;

public final class ServiceManager {

    public static interface InitListener {

        void initSucceed();
    }

    public static interface IAppConfig {

        void initLog(Context context);

        void initWorkDir(Context context);
    }

    public static abstract class AppConfig implements IAppConfig {

        /**
         * 初始化
         */
        public void init(Context context) {
            initWorkDir(context);
            initLog(context);
        }
    }

    private static final ApplicationImpl mApplication = new ApplicationImpl();

    static void appInit(Context context, AppConfig appConfig, Class<? extends IServiceManager> serviceClass) {
        mApplication.appInit(context, appConfig, serviceClass);
    }
//    static void appInit(Context context, AppConfig appConfig,
//            Class<? extends AbstractCoreService> clazz) {
//        mApplication.appInit(context, appConfig, clazz);
//    }

    public static void addService(String name, IBinder binder) {
        mApplication.addService(name, binder);
    }

    public static IBinder getService(String name) {
        return mApplication.getService(name);
    }

    public static boolean isSystemReady() {
        return mApplication.isSystemReady();
    }

    public static void start(InitListener initListener) {
        mApplication.start(initListener);
    }

    public static void stop() {
        mApplication.stop();
    }

    public static boolean isStarted() {
        return mApplication.isStarted();
    }

}
