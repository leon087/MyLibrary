package cm.android.framework.core.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import cm.android.sdk.PersistentService;
import cm.android.sdk.WakeLockUtil;

public final class DaemonService extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("DaemonService");

    private final DaemonReceiver daemonReceiver = new DaemonReceiver();

    public static final String ACTION_START = "cm.android.framework.intent.action.DAEMON_START";

    public static final String ACTION_STOP = "cm.android.framework.intent.action.DAEMON_STOP";

    private PowerManager.WakeLock lock;

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
            logger.info("start daemon");
            DaemonManager.getInstance().startDaemon(this);
            //开启守护进程
//            Daemon.startDaemon(this, DaemonService.class, Daemon.INTERVAL_ON_SECOND);

//            final Context context = this;
//            //测试功能
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(40000);
//                        Daemon.stopDaemon(context, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.start();

        } else if (ACTION_STOP.equals(action)) {
            DaemonManager.getInstance().stopDaemon(this);
//            Daemon.stopDaemon(this, DaemonService.class, Daemon.INTERVAL_ON_SECOND);
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        lock = WakeLockUtil.acquire(this, "daemon");
        daemonReceiver.register(this);
        logger.info("DaemonService:onCreate");
    }

    @Override
    public void onDestroy() {
        daemonReceiver.unregister(this);
//        WakeLockUtil.release(lock);
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
