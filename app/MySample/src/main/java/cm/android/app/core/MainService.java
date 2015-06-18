package cm.android.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static final void start() {
        logger.info("start:");
        WakeLockUtil.acquire(MyManager.getAppContext(), "MainService", 3000);

        Intent intent = new Intent(MyManager.getAppContext(), MainService.class);
        intent.setPackage(MyManager.getAppContext().getPackageName());
        MyManager.getAppContext().startService(intent);
    }

    public static final void stop() {
        Intent intent = new Intent(MyManager.getAppContext(), MainService.class);
        intent.setPackage(MyManager.getAppContext().getPackageName());
        MyManager.getAppContext().stopService(intent);
    }
}
