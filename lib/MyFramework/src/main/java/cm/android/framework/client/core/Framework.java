package cm.android.framework.client.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Looper;

import cm.android.applications.AppUtil;
import cm.android.framework.client.ipc.BinderFactory;
import cm.android.framework.client.ipc.ServiceManagerNative;
import cm.android.framework.component.CoreReceiver;
import cm.android.framework.server.IBinderServer;
import cm.android.framework.server.ServerProvider;
import cm.android.framework.server.daemon.DaemonService;
import cm.android.util.SystemUtil;

public class Framework {
    public static String SERVER_PROCESS_NAME = ":framework";

    public static String SERVER_NAME = "";

    private static final Framework gCore = new Framework();
    private Context context;
    private String processName;
    private ProcessType processType;
    private boolean isStartUp;
    private ConditionVariable initLock = new ConditionVariable();

    private String mainProcessName;

    private enum ProcessType {
        /**
         * Server process
         */
        SERVER,
        /**
         * Main process
         */
        MAIN,
        /**
         * Child process
         */
        CHILD
    }

    public static Framework get() {
        return gCore;
    }

    public Context getBaseContext() {
        return context;
    }

    public String getProcessName() {
        return processName;
    }

    public boolean isStartup() {
        return isStartUp;
    }

    public boolean isServerProcess() {
        return ProcessType.SERVER == processType;
    }

    public ConditionVariable getInitLock() {
        return initLock;
    }

    public void startup(Context context, Class<? extends IBinderServer> serviceClass) {
        if (isStartUp) {
            return;
        }

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("startup() must called in main thread.");
        }
        this.context = context;
        detectProcessType();

        init(serviceClass);

        isStartUp = true;
        if (initLock != null) {
            initLock.open();
            initLock = null;
        }
    }

    private void detectProcessType() {
        // Main process name
        mainProcessName = context.getApplicationInfo().processName;
        // Current process name
        processName = SystemUtil.getCurProcessName();
        if (processName.equals(mainProcessName)) {
            processType = ProcessType.MAIN;
        } else if (processName.endsWith(SERVER_PROCESS_NAME)) {
            processType = ProcessType.SERVER;
        } else {
            processType = ProcessType.CHILD;
        }
    }

    private void init(Class<? extends IBinderServer> serviceClass) {
        String processName = SystemUtil.getCurProcessName();

        if (serviceClass == null) {
            throw new IllegalArgumentException("serviceClass = null");
        }
        String serviceName = serviceClass.getName();
        SERVER_NAME = serviceName;

        DaemonService.bind(context, serviceConnection);

        PackageInfo packageInfo = AppUtil.getPackageInfo(context.getPackageManager(), context.getPackageName(), 0);
        if (packageInfo == null) {
            LogUtil.getLogger().error("packageInfo = null,getPackageName() = {},processName = {}", context.getPackageName(), SystemUtil.getCurProcessName());
        } else {
            LogUtil.getLogger().info("versionCode = {},versionName = {},processName = {}", packageInfo.versionCode, packageInfo.versionName, processName);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtil.getLogger().info("onServiceConnected:iBinder = {},processName = {}", iBinder, SystemUtil.getCurProcessName());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtil.getLogger().error("onServiceDisconnected:processName = {}", SystemUtil.getCurProcessName());
        }
    };

    /**
     * 开启业务层功能
     */
    public void start() {
        LogUtil.getLogger().info("framework:start");
        StateHelper.writeState(context, true);
        startInternal();
    }

    private void startInternal() {
        ServerProvider.Proxy.create(context);
    }

    /**
     * 关闭业务层功能
     */
    public void stop() {
        LogUtil.getLogger().info("framework:stop");
        StateHelper.writeState(context, false);
        ServerProvider.Proxy.destroy(context);
    }

    public boolean isActive() {
        Bundle response = ServerProvider.Proxy.isActive(context);
        if (response == null) {
            return false;
        }

        return response.getBoolean(ServerProvider.KEY_BINDER);
    }

    public static IBinder getService(String name) {
        return ServiceManagerNative.getService(name);
    }

    public static <T> T getService(String name, Class<? extends BinderFactory.IBinderProxy> proxyClass) {
        return BinderFactory.getProxy(name, proxyClass);
    }

    public static void addService(String name, IBinder service) {
        ServiceManagerNative.addService(name, service);
    }

    public static void clearService() {
        ServiceManagerNative.clearService();
    }

    public static void restoreService(Context context, String processName) {
        CoreReceiver.restore(context, processName);
    }
}
