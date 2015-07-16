package cm.android.framework.core.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import cm.android.sdk.PersistentService;
import cm.android.sdk.WakeLockUtil;

public final class DaemonService extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("DaemonService");

    private final DaemonReceiver daemonReceiver = new DaemonReceiver();

    public static final String ACTION_START = "cm.android.framework.intent.action.DAEMON_START";

    public static final String ACTION_STOP = "cm.android.framework.intent.action.DAEMON_STOP";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onServiceStart(Intent intent, int flags, int startId) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        logger.info("intent = " + intent);

        if (ACTION_START.equals(action)) {
            DaemonManager.getInstance().startDaemon(this);
        } else if (ACTION_STOP.equals(action)) {
            DaemonManager.getInstance().stopDaemon(this);
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        daemonReceiver.register(this);
        logger.info("DaemonService:onCreate");
    }

    @Override
    public void onDestroy() {
        daemonReceiver.unregister(this);
        super.onDestroy();
        logger.info("DaemonService:onDestroy");
    }

    public static void start(Context context) {
        WakeLockUtil.acquire(context, "framework", 1000);

        Intent intent = new Intent(context, DaemonService.class);
        intent.setPackage(context.getPackageName());
        intent.setAction(ACTION_START);
        context.startService(intent);
    }

    public static void stop(Context context) {
        WakeLockUtil.acquire(context, "framework", 1000);

        Intent intent = new Intent(context, DaemonService.class);
        intent.setPackage(context.getPackageName());
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }
}
