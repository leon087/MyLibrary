package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cm.android.framework.client.core.Framework;
import cm.android.framework.client.core.LogUtil;
import cm.android.sdk.WakeLockUtil;

public class MainService extends Service {

    private static final Logger logger = LoggerFactory.getLogger("core");

    private static final String ACTION_CREATE = "ggg.create";
    private static final String ACTION_DESTROY = "ggg.destroy";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final void start(Context context) {
        logger.info("start:");
        WakeLockUtil.acquire(context, "MainService", 3000);

        Intent intent = new Intent(context, MainService.class);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
    }

    public static final void start(Context context, String action) {
        logger.info("start:");
        WakeLockUtil.acquire(context, "MainService", 3000);

        Intent intent = new Intent(context, MainService.class);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
    }

    public static final void stop(Context context) {
        Intent intent = new Intent(context, MainService.class);
        intent.setPackage(context.getPackageName());
        context.stopService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.getLogger().error("gggg:MainService:onDestroy");

        Bundle bundle = new Bundle();
        bundle.putBoolean("exit", true);
        Framework.get().putBundle("ggg", bundle);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.getLogger().error("gggg:MainService:onCreate");
        Bundle bundle = new Bundle();
        bundle.putBoolean("exit", false);
        Framework.get().putBundle("ggg", bundle);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //start
        return super.onStartCommand(intent, flags, startId);
    }
}
