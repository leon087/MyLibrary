package cm.android.app.dm;

import org.slf4j.LoggerFactory;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.logging.Logger;

import cm.android.timer.Timer;
import cm.android.timer.TimerTask;

public class AdminDaemonService extends Service {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("AdminDaemon");

    public static void start(Context context) {
        Intent intent = new Intent(context, AdminDaemonService.class);
        intent.setPackage(context.getPackageName());
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            if (startId % 3 == 0) {
                startService(new Intent(this, this.getClass()));
                return START_NOT_STICKY;
            }
        }

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer("AdminDaemon");
        timer.schedule(timerTask, 200, 5 * 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        timer.cancel();
        timer = null;
    }

    private Timer timer;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            boolean isAdminActive = PolicyManagerImpl.getInstance(AdminDaemonService.this)
                    .isAdminActive();
            logger.info("isAdminActive = {}", isAdminActive);
            if (!isAdminActive) {
                activeAdmin(AdminDaemonService.this);
            } else {
                AdminDaemonService.this.stopSelf();
            }
        }
    };

    private void activeAdmin(Context context) {
        Intent activityIntent = new Intent(context, AdminDaemonActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
