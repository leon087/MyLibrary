package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.applications.AppUtil;
import cm.android.framework.core.daemon.DaemonService;
import cm.android.util.SystemUtil;

final class ApplicationImpl {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final AtomicBoolean startAtomic = new AtomicBoolean(false);

    private Context appContext;

    private IServiceManager serviceManager;

    private final ServiceBinderProxy serviceBidnerProxy = new ServiceBinderProxy();

    private InitListener initListener;

    final boolean isStarted() {
        return startAtomic.get();
    }

    void appInit(Context context, AppConfig appConfig, IServiceManager serviceManager) {
        if (appContext != null) {
            logger.error(
                    "old.appContext = {},old.processName = {},new.context = {},new.processName = {}",
                    appContext, SystemUtil.getCurProcessName(appContext),
                    context, SystemUtil.getCurProcessName(context));
            throw new IllegalArgumentException("appContext = " + appContext);
        }

        appContext = context.getApplicationContext();
        this.serviceManager = serviceManager;
        appConfig.init(appContext);
        CoreService.bind(appContext, mServiceConnection);

        PackageInfo packageInfo = AppUtil.getPackageInfo(
                appContext.getPackageManager(), appContext.getPackageName());
        logger.info("versionCode = {},versionName = {},processName = {}", packageInfo.versionCode
                , packageInfo.versionName, SystemUtil.getCurProcessName(appContext));
    }

    final synchronized void start(InitListener initListener) {
        this.initListener = initListener;
        start();
    }

    private void start() {
        StateHolder.writeState(appContext, true);

        if (!isBindService()) {
            logger.error("start:serviceBidner = null");
            return;
        }

        startAtomic.set(true);
        DaemonService.start(appContext);
        serviceBidnerProxy.create();
        notifyInitSucceed();
    }

    final synchronized void stop() {
        StateHolder.writeState(appContext, false);

        startAtomic.set(false);
        DaemonService.stop(appContext);
        this.initListener = null;

        serviceBidnerProxy.destroy();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            logger.info("onServiceConnected:componentName = {},iBinder = {},processName = {}",
                    componentName, iBinder, SystemUtil.getCurProcessName(appContext));
            serviceBidnerProxy.bindServiceBinder(IServiceBinder.Stub.asInterface(iBinder));
            serviceBidnerProxy.initService(serviceManager);

            //状态恢复
            if (StateHolder.isStateInit(appContext)) {
                start();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            logger.error("onServiceDisconnected:componentName = " + componentName);
            serviceBidnerProxy.bindServiceBinder(null);
        }
    };

    final IBinder getService(String name) {
        return serviceBidnerProxy.getService(name);
    }

    final void addService(String name, IBinder binder) {
        serviceBidnerProxy.addService(name, binder);
    }

    final boolean isBindService() {
        return serviceBidnerProxy.isBindService();
    }

    private void notifyInitSucceed() {
        if (null != initListener) {
            initListener.initSucceed();
            this.initListener = null;
        }
    }

    private static class StateHolder {

        private static boolean isStateInit(Context context) {
            return readState(context);
        }

        private static void writeState(Context context, boolean state) {
            SharedPreferences preferences = context.getSharedPreferences("app_status",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("state", state);
            editor.commit();
        }

        private static boolean readState(Context context) {
            SharedPreferences preferences = context.getSharedPreferences("app_status",
                    Context.MODE_PRIVATE);
            return preferences.getBoolean("state", false);
        }
    }

    private static class ServiceBinderProxy extends IServiceBinder.Stub {

        private static final Logger logger = LoggerFactory.getLogger("framework");

        private IServiceBinder serviceBinder;

        ServiceBinderProxy() {

        }

        void bindServiceBinder(IServiceBinder serviceBinder) {
            this.serviceBinder = serviceBinder;
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
