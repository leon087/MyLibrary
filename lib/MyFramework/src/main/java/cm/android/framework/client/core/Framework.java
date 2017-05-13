package cm.android.framework.client.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import cm.android.applications.AppUtil;
import cm.android.framework.client.ipc.BinderFactory;
import cm.android.framework.client.ipc.ServiceManager;
import cm.android.framework.component.CoreReceiver;
import cm.android.framework.component.IBinderServer;
import cm.android.framework.server.ServerProvider;
import cm.android.framework.server.daemon.DaemonService;
import cm.android.util.SystemUtil;
import cm.java.util.Utils;

public class Framework {

    /**
     * Server进程后缀
     */
    private static String SERVER_PROCESS_SUFFIX_DEF = ":framework";

    public static String SERVER_NAME = "";

    private static final Framework gCore = new Framework();
    private Context context;
    private String processName;
    private String serverProcessName;
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
        if (Utils.isEmpty(serverProcessName)) {
            serverProcessName = mainProcessName + SERVER_PROCESS_SUFFIX_DEF;
        }
        // Current process name
        processName = SystemUtil.getCurProcessName(context);
        if (processName.equals(mainProcessName)) {
            processType = ProcessType.MAIN;
        } else if (processName.equals(serverProcessName)) {
            processType = ProcessType.SERVER;
        } else {
            processType = ProcessType.CHILD;
        }
    }

    private void init(Class<? extends IBinderServer> serverClass) {
        if (serverClass == null) {
            throw new IllegalArgumentException("serverClass = null");
        }
        SERVER_NAME = serverClass.getName();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // TODO: ggg 2017/3/16 : nubia|coolpad上会导致ServerProvider.onCreate执行两次
                DaemonService.bind(context, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        LogUtil.getLogger().info("onServiceConnected:iBinder = {},processName = {}", iBinder, SystemUtil.getCurProcessName(context));
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        LogUtil.getLogger().error("onServiceDisconnected:processName = {}", SystemUtil.getCurProcessName(context));
                    }
                });
            }
        });

        PackageInfo packageInfo = AppUtil.getPackageInfo(context.getPackageManager(), context.getPackageName(), 0);
        if (packageInfo == null) {
            LogUtil.getLogger().error("packageInfo = null,getPackageName() = {},processName = {}", context.getPackageName(), SystemUtil.getCurProcessName(context));
        } else {
            LogUtil.getLogger().info("versionCode = {},versionName = {},processName = {}", packageInfo.versionCode, packageInfo.versionName, processName);
        }
    }

    /**
     * 开启业务层功能
     */
    public void start() {
        LogUtil.getLogger().info("framework:start");
        StateHelper.writeState(context, true);
        ServerProvider.Proxy.create(context);
//        Binder.getCallingPid()
//        Binder.getCallingUid()
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

    public void putBundle(String key, Bundle bundle) {
        ServerProvider.Proxy.putBundle(context, key, bundle);
    }

    public Bundle getBundle(String key) {
        Bundle response = ServerProvider.Proxy.getBundle(context, key);
        if (response == null) {
            return null;
        }
        return response.getBundle(ServerProvider.KEY_BINDER);
    }

    public static IBinder getService(String name) {
//        return ServiceManagerNative.getService(name);
        return ServiceManager.getService(name);
    }

    public static <T> T getBinderProxy(String name, Class<? extends BinderFactory.IBinderProxy> proxyClass) {
        return BinderFactory.getProxy(name, proxyClass);
    }

    public static void addService(String name, IBinder service) {
//        ServiceManagerNative.addService(name, service);
        ServiceManager.addService(name, service);
    }

    public static void clearService() {
//        ServiceManagerNative.clearService();
        ServiceManager.clearService();
    }

    public static void restoreService(Context context, String processName) {
        CoreReceiver.restore(context, processName);
    }

    public void config(Config config) {
        ServerProvider.authorities(config.getAuthorities());
        serverProcessName = config.getServerProcess();
    }

    private Framework() {
    }
}
