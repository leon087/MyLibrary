package cm.android.framework.core;

import android.content.Intent;
import android.os.IBinder;
import cm.android.sdk.PersistentService;

public class CoreService extends PersistentService {

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
