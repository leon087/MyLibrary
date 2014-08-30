package cm.android.framework.core;

import android.content.Intent;
import android.os.IBinder;
import cm.android.sdk.PersistentService;

public class CoreService extends PersistentService {
    private final CoreReceiver coreReceiver = new CoreReceiver();

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
        coreReceiver.registerReceiver(this);
    }

    @Override
    public void onDestroy() {
        coreReceiver.unRegisterReceiver();
        super.onDestroy();
    }
}
