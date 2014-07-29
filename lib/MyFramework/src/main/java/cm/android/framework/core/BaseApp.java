package cm.android.framework.core;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import cm.android.applications.AppUtil;
import cm.android.framework.core.manager.BaseManager;
import cm.android.util.ActivityStack;
import cm.android.util.EnvironmentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseApp extends Application implements IApp {
    private static BaseApp sApp = null;
    private volatile boolean isInit = false;
    private BaseManager mServiceManager;
    private static final Logger logger = LoggerFactory.getLogger(BaseApp.class);

    public static boolean isInit() {
        if (sApp == null) {
            return false;
        }
        return sApp.isInit;
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
        disableConnectionReuseIfNecessary();
        PackageInfo packageInfo = AppUtil.getPackageInfo(
                this.getPackageManager(), this.getPackageName());
        logger.info("versionCode = {},versionName = {}", packageInfo.versionCode
                , packageInfo.versionName);
    }

    /**
     * 禁用连接池
     */
    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (!EnvironmentInfo.SdkUtil.hasFroyo()) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * 在假设Application不释放的情况下，进入app业务态，初始化资源
     */
    @Override
    public synchronized void initApp() {
        logger.info("isInit = " + isInit);
        isInit = true;

        this.startService(new Intent(this, CoreService.class));
        if (mServiceManager == null) {
            mServiceManager = initServiceManager();
            if (mServiceManager != null) {
                mServiceManager.create(sApp);
            }
        }
    }

    /**
     * 退出app业务态，释放资源（注：结束进程只做辅助用）
     */
    @Override
    public synchronized void exitApp() {
        logger.info("isInit = " + isInit);
        isInit = false;
        ActivityStack.getInstance().finishAll();

        stopService(new Intent(this, CoreService.class));
        if (null != mServiceManager) {
            mServiceManager.destroy();
            // 置null，以使得虚拟机主动回收该对象中资源
            mServiceManager = null;
            System.gc();
        }
    }

    public void exitAppProcess() {
        // 退出，由于android不建议直接结束进程，故此处只做辅助用
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 初始化服务
     *
     * @return
     */
    protected abstract BaseManager initServiceManager();

    /**
     * 初始化App运行配置
     */
    protected abstract AppConfig initConfig();
}
