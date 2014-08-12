package cm.android.sdk;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;

public abstract class PersistentService extends Service {

    @Override
    @TargetApi(5)
    public final int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            if (startId % 3 == 0) {
                startService(new Intent(this, this.getClass()));
                return START_NOT_STICKY;
            }
        }

        onStartService(intent, flags, startId);

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public abstract void onStartService(Intent intent, int flags, int startId);
}
