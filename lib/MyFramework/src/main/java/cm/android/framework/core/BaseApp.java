package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.applications.AppUtil;
import cm.android.framework.core.manager.BaseManager;
import cm.android.util.ActivityStack;
import cm.android.util.BuildConfigUtil;
import cm.android.util.EnvironmentUtil;

public abstract class BaseApp extends Application implements IApp {

    private static BaseApp sApp = null;

    private final AtomicBoolean isInitAtomic = new AtomicBoolean(false);

    private BaseManager mServiceManager;

    private static final Logger logger = LoggerFactory.getLogger(BaseApp.class);

    public static boolean isInit() {
        if (sApp == null) {
            return false;
        }
        return sApp.isInitAtomic.get();
    }

    public static boolean isDebug() {
        if (sApp == null) {
            return false;
        }
        return BuildConfigUtil.isDebug(sApp);
    }

    private boolean isStateInit() {
        return readState();
    }

    private void writeState(boolean state) {
        SharedPreferences preferences = this
                .getSharedPreferences("app_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("state", state);
        editor.commit();
    }

    private boolean readState() {
        SharedPreferences preferences = this
                .getSharedPreferences("app_status", Context.MODE_PRIVATE);
        return preferences.getBoolean("state", false);
    }

    /**
     * 获取Application对象
     *
     * @return BaseApp
     */
    public synchronized static BaseApp getApp() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        initConfig().init(this);
        appInit();
        disableConnectionReuseIfNecessary();
        PackageInfo packageInfo = AppUtil.getPackageInfo(
                this.getPackageManager(), this.getPackageName());
        logger.info("versionCode = {},versionName = {}", packageInfo.versionCode
                , packageInfo.versionName);

        //状态恢复
        if (isStateInit()) {
            initApp();
        }
    }

    /**
     * 禁用连接池
     */
    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (!EnvironmentUtil.SdkUtil.hasFroyo()) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * app初始化，跟随application生命周期，初始化不释放的资源
     */
    @Override
    public final void appInit() {
        if (mServiceManager == null) {
            mServiceManager = initServiceManager();
            if (mServiceManager != null) {
                mServiceManager.init(sApp);
            }
        }
    }

    /**
     * 在假设Application不释放的情况下，进入app业务态，初始化资源
     */
    @Override
    public final synchronized void initApp() {
        logger.info("isInit = " + isInitAtomic.get());

        if (!isInitAtomic.compareAndSet(false, true)) {
            return;
        }

        writeState(true);

        this.startService(new Intent(this, CoreService.class));
        DaemonManager.getInstance().startDaemon(this);

        if (mServiceManager != null) {
            mServiceManager.create();
        }
    }

    /**
     * 退出app业务态，释放资源（注：结束进程只做辅助用）
     */
    @Override
    public final synchronized void exitApp() {
        logger.info("isInit = " + isInitAtomic.get());
        if (!isInitAtomic.compareAndSet(true, false)) {
            return;
        }

        writeState(false);
        ActivityStack.getInstance().finishAll();
        DaemonManager.getInstance().stopDaemon(this);

        stopService(new Intent(this, CoreService.class));
        if (null != mServiceManager) {
            mServiceManager.destroy();
            // 置null，以使得虚拟机主动回收该对象中资源
            System.gc();
        }
    }

    @TargetApi(8)
    public final void exitAppProcess() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        if (EnvironmentUtil.SdkUtil.hasFroyo()) {
            am.killBackgroundProcesses(this.getPackageName());
        } else {
            am.restartPackage(this.getPackageName());
        }
        // 退出，由于android不建议直接结束进程，故此处只做辅助用
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 初始化服务
     */
    protected abstract BaseManager initServiceManager();

    /**
     * 初始化App运行配置
     */
    protected abstract AppConfig initConfig();
}
