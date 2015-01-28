package cm.android.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;

public abstract class PersistentService extends Service {

    private static final Logger logger = LoggerFactory.getLogger("PersistentService");

    @Override
    public void onCreate() {
        super.onCreate();
        logger.info("onCreate:" + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.info("onDestroy:" + this);
    }

    @Override
    @TargetApi(5)
    public final int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            logger.info("flags = {},startId = {}", flags, startId);
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
