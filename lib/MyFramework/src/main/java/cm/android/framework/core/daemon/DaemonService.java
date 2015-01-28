package cm.android.framework.core.daemon;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import cm.android.sdk.PersistentService;

public class DaemonService extends PersistentService {

    private final DaemonReceiver daemonReceiver = new DaemonReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStartService(Intent intent, int flags, int startId) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        daemonReceiver.register(this);
        DaemonManager.getInstance().startDaemon(this);
    }

    @Override
    public void onDestroy() {
        DaemonManager.getInstance().stopDaemon(this);
        daemonReceiver.unregister();
        super.onDestroy();
    }

    public static void start(Context context) {
        context.startService(new Intent(context, DaemonService.class));
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, DaemonService.class));
    }
}
