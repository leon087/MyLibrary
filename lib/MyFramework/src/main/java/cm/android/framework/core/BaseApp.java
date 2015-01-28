package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.applications.AppUtil;
import cm.android.framework.core.daemon.DaemonService;
import cm.android.framework.core.manager.IServiceManager;
import cm.android.util.BuildConfigUtil;
import cm.android.util.EnvironmentUtil;
import cm.android.util.SystemUtil;

public abstract class BaseApp extends Application implements IApp {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private static BaseApp sApp = null;

    private final AtomicBoolean isInitAtomic = new AtomicBoolean(false);

    public static final boolean isInit() {
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

    /**
     * 获取Application对象
     *
     * @return BaseApp
     */
    public synchronized final static BaseApp getApp() {
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
        logger.info("versionCode = {},versionName = {},processName = {}", packageInfo.versionCode
                , packageInfo.versionName, SystemUtil.getCurProcessName(this));
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

    public final void appInit() {
        CoreService.bind(BaseApp.this, mServiceConnection);
    }

    @Override
    public final synchronized void initApp(InitListener initListener) {
        this.initListener = initListener;
        initApp();
    }

    private void initApp() {
        StateHolder.writeState(this, true);

        if (!isBindService()) {
            logger.error("initApp:serviceBidner = null");
            return;
        }

        isInitAtomic.set(true);
        DaemonService.start(this);
        serviceBidner.create();
        notifyInitSucceed();
    }

    @Override
    public final synchronized void exitApp() {
        StateHolder.writeState(this, false);

        isInitAtomic.set(false);
        DaemonService.stop(this);
        this.initListener = null;

        if (serviceBidner != null) {
            serviceBidner.destroy();
        }
    }

    /**
     * 初始化App运行配置
     */
    protected abstract AppConfig initConfig();

    protected abstract IServiceManager initService();

    private ServiceBidnerImpl serviceBidner;

    private InitListener initListener;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            logger.info("onServiceConnected:componentName = {},iBinder = {}", componentName,
                    iBinder);
            serviceBidner = (ServiceBidnerImpl) iBinder;
            serviceBidner.initService(initService());

            //状态恢复
            if (StateHolder.isStateInit(BaseApp.this)) {
                initApp();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            logger.error("onServiceDisconnected:componentName = " + componentName);
            serviceBidner = null;
        }
    };

    final <T> T getService(String name) {
        if (!isBindService()) {
            logger.error("name = " + name);
            return null;
        }
        return serviceBidner.getService(name);
    }

    final void addService(String name, Object manager) {
        if (!isBindService()) {
            logger.error("name = {},manager = {}", name, manager);
            return;
        }
        serviceBidner.addService(name, manager);
    }

    final boolean isBindService() {
        if (serviceBidner == null) {
            logger.info("serviceBidner = null");
            return false;
        }
        return true;
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
}