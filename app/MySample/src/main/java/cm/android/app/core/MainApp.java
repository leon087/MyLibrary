package cm.android.app.core;

import com.squareup.leakcanary.LeakCanary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import cm.android.framework.client.core.Config;
import cm.android.framework.client.core.Framework;
import cm.android.framework.server.ServerProvider;
import cm.android.util.AndroidUtils;
import cm.android.util.SystemUtil;

public class MainApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private static MainApp sMainApp;

    private final DaemonReceiver daemonReceiver = new DaemonReceiver();

    @Override
    protected void attachBaseContext(Context base) {
        new MainConfig().init(base);

        super.attachBaseContext(base);
        MultiDex.install(this);

        Framework.get().config(new Config.Builder()
                .authorities(base.getPackageName() + "." + ServerProvider.AUTHORITIES)
                .serverProcess(base.getApplicationInfo().processName + ":framework")
                .build());
        Framework.get().startup(base, MyBinderServer.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.error("ggg application onCreate");
        android.util.Log.e("ggggggg", "ggggggg application onCreate");
        sMainApp = this;
        ObjectPool.init(this);

//        setStrictMode(this);
        LeakCanary.install(this);

        daemonReceiver.registerLocal(this);

        String pName = SystemUtil.getCurProcessName();
    }

    public static MainApp getApp() {
        return sMainApp;
    }

    private void setStrictMode(Context context) {
        if (AndroidUtils.isDebuggable(context)) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyDialog()
                    .penaltyLog()
                    .penaltyDeathOnNetwork()
                    .build());
        }
    }
}