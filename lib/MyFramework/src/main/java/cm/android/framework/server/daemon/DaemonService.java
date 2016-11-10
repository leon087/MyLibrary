package cm.android.framework.server.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
            logger.info("start daemon");
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

        try {
            Notification notification = new Notification();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            startForeground(0, notification);
        } catch (Throwable e) {
            // Ignore
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
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

    public final static boolean bind(Context context, ServiceConnection connection) {
        Intent intent = new Intent(context, DaemonService.class);
        intent.setPackage(context.getPackageName());
        return context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public final static void unbind(Context context, ServiceConnection connection) {
        context.unbindService(connection);
    }
}
