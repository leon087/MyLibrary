package cm.android.framework.core;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CoreService extends Service {
    public static final String ACTION_MAIN_SERVICE = "cm.android.framework.CORE_SERVICE";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @TargetApi(5)
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = ACTION_MAIN_SERVICE;
        if (null == intent) {
            if (startId % 3 == 0) {
                startService(new Intent(ACTION_MAIN_SERVICE));
                return START_NOT_STICKY;
            }
        } else {
            action = intent.getAction();
        }

        super.onStartCommand(intent, flags, startId);
        return processAction(action);
    }

    private int processAction(String action) {
        if (ACTION_MAIN_SERVICE.equals(action)) {
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
