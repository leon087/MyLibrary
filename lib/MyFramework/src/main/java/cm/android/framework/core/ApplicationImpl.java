package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.applications.AppUtil;
import cm.android.framework.core.daemon.DaemonService;
import cm.android.util.IntentUtil;
import cm.android.util.SystemUtil;
import cm.java.util.IoUtil;

final class ApplicationImpl {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final AtomicBoolean startAtomic = new AtomicBoolean(false);

    private Context appContext;

    private final ServiceBinderProxy serviceBidnerProxy = new ServiceBinderProxy();

    //    private ServiceManager.InitListener initListener;
    private WeakReference<ServiceManager.InitListener> initListener;

    final synchronized boolean isStarted() {
//        return startAtomic.get();
        return serviceBidnerProxy.isInit();
    }

    void appInit(Context context, ServiceManager.AppConfig appConfig,
                 Class<? extends IServiceManager> serviceClass) {
        if (appContext != null) {
            logger.error(
                    "old.appContext = {},old.processName = {},new.context = {},new.processName = {}",
                    appContext, SystemUtil.getCurProcessName(appContext),
                    context, SystemUtil.getCurProcessName(context));
            throw new IllegalArgumentException("appContext = " + appContext);
        }

        if (serviceClass == null) {
            throw new IllegalArgumentException("serviceClass = null");
        }
        String serviceName = serviceClass.getName();

        appContext = context.getApplicationContext();
        appConfig.init(appContext);
        CoreService.bind(appContext, mServiceConnection, serviceName);

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

    final void handleAction(String action, Bundle bundle) {
        CoreService.start(appContext, action, bundle);
    }

    final void start(ServiceManager.InitListener initListener) {
        logger.info("start");
        if (startAtomic.get()) {
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
        if (!startAtomic.get()) {
            DaemonService.start(appContext);
        } else {
            logger.error("startInternal:startAtomic = " + startAtomic.get());
        }

        serviceBidnerProxy.create();
        notifyInitSucceed();
        startAtomic.set(true);
    }

    private synchronized void stopInternal() {
        this.initListener = null;
        serviceBidnerProxy.destroy();

        if (startAtomic.get()) {
            DaemonService.stop(appContext);
        }
        startAtomic.set(false);
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

        notifyBindSucceed(appContext);
    }

    public final static void notifyBindSucceed(Context context) {
        Intent intent = new Intent(ServiceManager.ACTION_BIND_SUCCEED);
        //绑定成功发送本地广播
        IntentUtil.sendBroadcastLocal(context, intent);
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

        private static final String STATE_FILE_NAME = "framework_app_status";

        private static final String TAG_STATE = "state";

        private static boolean isStateInit(Context context) {
            return readState(context);
        }

        private static void writeState(Context context, boolean state) {
            logger.info("writeState:state = " + state);

            File file = new File(context.getFilesDir(), STATE_FILE_NAME);
            Properties properties = IoUtil.loadProperties(file);
            properties.setProperty(TAG_STATE, String.valueOf(state));

            OutputStream os = null;
            try {
                os = new FileOutputStream(file);
                properties.store(os, "writeState:state = " + state);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                IoUtil.closeQuietly(os);
            }
        }

        private static boolean readState(Context context) {
            File file = new File(context.getFilesDir(), STATE_FILE_NAME);
            if (!file.exists()) {
                return false;
            }

            Properties properties = IoUtil.loadProperties(file);
            boolean state = Boolean.valueOf(
                    properties.getProperty(TAG_STATE, String.valueOf(false)));

            logger.info("readState:state = " + state);
            return state;
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
            return serviceBinder.asBinder().isBinderAlive();
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
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        @Override
        public IBinder getService(String name) {
            if (!isBindService()) {
                logger.error("getService:name = " + name);
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
                logger.error("addService:name = {},binder = {}", name, binder);
                return;
            }

            try {
                serviceBinder.addService(name, binder);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
            }
        }

        @Override
        public boolean isInit() {
            if (!isBindService()) {
                logger.error("isInit:isBindService = false");
                return false;
            }

            try {
                return serviceBinder.isInit();
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }
    }
}
