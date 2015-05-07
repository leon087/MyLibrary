package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.applications.AppUtil;
import cm.android.framework.core.daemon.DaemonService;
import cm.android.util.EnvironmentUtil;
import cm.android.util.SystemUtil;

final class ApplicationImpl {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final AtomicBoolean startAtomic = new AtomicBoolean(false);

    private Context appContext;

    private IServiceManager serviceManager;

    private final ServiceBinderProxy serviceBidnerProxy = new ServiceBinderProxy();

    //    private ServiceManager.InitListener initListener;
    private WeakReference<ServiceManager.InitListener> initListener;

    final synchronized boolean isStarted() {
        return startAtomic.get();
    }

    void appInit(Context context, ServiceManager.AppConfig appConfig,
            IServiceManager serviceManager) {
        if (appContext != null) {
            logger.error(
                    "old.appContext = {},old.processName = {},new.context = {},new.processName = {}",
                    appContext, SystemUtil.getCurProcessName(appContext),
                    context, SystemUtil.getCurProcessName(context));
            throw new IllegalArgumentException("appContext = " + appContext);
        }

        if (serviceManager == null) {
            logger.error("serviceManager = null,new.context = {},new.processName = {}", context,
                    SystemUtil.getCurProcessName(context));
            throw new IllegalArgumentException("serviceManager = null");
        }

        appContext = context.getApplicationContext();

        appConfig.init(appContext);

        if (EnvironmentUtil.SdkUtil.hasJellyBeanMr2()) {
            CoreService.bind(appContext, mServiceConnection, serviceManager);
        } else {
            this.serviceManager = serviceManager;
            CoreService.bind(appContext, mServiceConnection);
        }

        PackageInfo packageInfo = AppUtil.getPackageInfo(
                appContext.getPackageManager(), appContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);

        if (packageInfo == null) {
            logger.error("packageInfo = null,getPackageName() = {},processName = {}",
                    appContext.getPackageName(), SystemUtil.getCurProcessName(appContext));
        } else {
            logger.info("versionCode = {},versionName = {},processName = {}",
                    packageInfo.versionCode, packageInfo.versionName,
                    SystemUtil.getCurProcessName(appContext));
        }
    }

    final void start(ServiceManager.InitListener initListener) {
        logger.info("start");
        if (isStarted()) {
            logger.error("start:isStarted = true");
            return;
        }

        StateHolder.writeState(appContext, true);

        this.initListener = new WeakReference<ServiceManager.InitListener>(initListener);

        if (isSystemReady()) {
            startInternal();
        } else {
            logger.error("start:isSystemReady = false");
        }
    }

    private synchronized void startInternal() {
        if (startAtomic.compareAndSet(false, true)) {
            DaemonService.start(appContext);
        } else {
            logger.error("startInternal:startAtomic = " + startAtomic.get());
        }

        serviceBidnerProxy.create();
        notifyInitSucceed();
    }

    private synchronized void stopInternal() {
        this.initListener = null;
        serviceBidnerProxy.destroy();

        if (startAtomic.compareAndSet(true, false)) {
            DaemonService.stop(appContext);
        }

    }

    final void stop() {
        logger.info("stop");
        StateHolder.writeState(appContext, false);

        stopInternal();
    }

    private void systemReady() {
        //状态恢复
        if (StateHolder.isStateInit(appContext)) {
            startInternal();
        }
    }

    private void systemFailed() {
        if (StateHolder.isStateInit(appContext)) {
            stopInternal();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            logger.info("onServiceConnected:iBinder = {},processName = {}", iBinder,
                    SystemUtil.getCurProcessName(appContext));

            if (iBinder == null) {
                logger.error("iBinder = null");
                return;
            }

            serviceBidnerProxy.bindServiceBinder(iBinder);
            if (serviceManager != null) {
                serviceBidnerProxy.initService(serviceManager);
            }

            systemReady();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            logger.error("onServiceDisconnected:processName = {}",
                    SystemUtil.getCurProcessName(appContext));
            serviceBidnerProxy.bindServiceBinder(null);

            systemFailed();
        }
    };

    final IBinder getService(String name) {
        return serviceBidnerProxy.getService(name);
    }

    final void addService(String name, IBinder binder) {
        serviceBidnerProxy.addService(name, binder);
    }

//    final boolean isBindService() {
//        return serviceBidnerProxy.isBindService();
//    }

    final boolean isSystemReady() {
        return serviceBidnerProxy.isBindService();
    }

    private void notifyInitSucceed() {
        if (null != initListener && initListener.get() != null) {
            initListener.get().initSucceed();
            this.initListener = null;
        }
    }

    private static class StateHolder {

        private static boolean isStateInit(Context context) {
            return readState(context);
        }

        private static void writeState(Context context, boolean state) {
            SharedPreferences preferences = context.getSharedPreferences("framework_app_status",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("state", state);
            editor.commit();
        }

        private static boolean readState(Context context) {
            SharedPreferences preferences = context.getSharedPreferences("framework_app_status",
                    Context.MODE_PRIVATE);
            return preferences.getBoolean("state", false);
        }
    }

    private static class ServiceBinderProxy extends IServiceBinder.Stub {

        private static final Logger logger = LoggerFactory.getLogger("framework");

        private IServiceBinder serviceBinder;

        ServiceBinderProxy() {
        }

        void bindServiceBinder(IBinder iBinder) {
            this.serviceBinder = IServiceBinder.Stub.asInterface(iBinder);
        }

        boolean isBindService() {
            if (serviceBinder == null) {
                return false;
            }
            return true;
        }

        @Override
        public void initService(IServiceManager serviceManager) {
            if (!isBindService()) {
                return;
            }

            try {
                serviceBinder.initService(serviceManager);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
            }
        }

        @Override
        public void create() {
            if (!isBindService()) {
                return;
            }

            try {
                serviceBinder.create();
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
            }
        }

        @Override
        public void destroy() {
            if (!isBindService()) {
                return;
            }

            try {
                serviceBinder.destroy();
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
            }
        }

        @Override
        public IBinder getService(String name) {
            if (!isBindService()) {
                logger.error("name = " + name);
                return null;
            }

            try {
                return serviceBinder.getService(name);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }

        @Override
        public void addService(String name, IBinder binder) {
            if (!isBindService()) {
                logger.error("name = {},binder = {}", name, binder);
                return;
            }

            try {
                serviceBinder.addService(name, binder);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
