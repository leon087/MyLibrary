package cm.android.framework.core;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class FrameworkService extends Service {

    private final AtomicBoolean create = new AtomicBoolean(false);

    @Override
    public final void onCreate() {
        super.onCreate();
        if (!ServiceManager.isStarted()) {
            stopSelf();
            return;
        }

        create.set(true);
        onServiceCreate();
    }

    @Override
    public final void onDestroy() {
        if (create.compareAndSet(true, false)) {
            onServiceDestroy();
        }

        super.onDestroy();
    }

    @Override
    @TargetApi(5)
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            if (startId % 3 == 0) {
                startService(new Intent(this, this.getClass()));
                return START_NOT_STICKY;
            }
        }

        if (create.get()) {
            onServiceStart(intent, flags, startId);
        }

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    protected abstract void onServiceCreate();

    protected abstract void onServiceDestroy();

    protected abstract void onServiceStart(Intent intent, int flags, int startId);
}
