package cm.android.framework.server.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cm.android.sdk.PersistentService;
import cm.android.sdk.WakeLockUtil;

public final class DaemonService extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("DaemonService");

    private final DaemonReceiver daemonReceiver = new DaemonReceiver();

    public static final String ACTION_START = "cm.android.framework.intent.action.DAEMON_START";

    public static final String ACTION_STOP = "cm.android.framework.intent.action.DAEMON_STOP";

    private static final int NOTIFY_ID = 10000;

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

        startService(new Intent(this, InnerService.class));
        startForeground(NOTIFY_ID, new Notification());
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        daemonReceiver.unregister(this);
        super.onDestroy();
        logger.info("DaemonService:onDestroy");
    }

    public static final class InnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(NOTIFY_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public static void start(Object obj, Context context) {
        WakeLockUtil.acquire(context, "framework:" + obj.getClass().getName(), 1000);

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
        return context.bindService(intent, connection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT | Context.BIND_ABOVE_CLIENT);
    }

    public final static void unbind(Context context, ServiceConnection connection) {
        context.unbindService(connection);
    }
}
