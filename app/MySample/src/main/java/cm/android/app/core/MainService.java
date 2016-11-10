package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cm.android.framework.core.FrameworkService;
import cm.android.sdk.WakeLockUtil;

public class MainService extends FrameworkService {

    private static final Logger logger = LoggerFactory.getLogger("core");

    @Override
    protected void onServiceCreate() {

    }

    @Override
    protected void onServiceDestroy() {

    }

    @Override
    protected void onServiceStart(Intent intent, int flags, int startId) {

    }

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

    public static final void stop(Context context) {
        Intent intent = new Intent(context, MainService.class);
        intent.setPackage(context.getPackageName());
        context.stopService(intent);
    }
}
