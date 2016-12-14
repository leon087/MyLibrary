package cm.android.framework.component;

import android.app.Service;
import android.content.Intent;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.framework.client.core.Framework;
import cm.android.framework.client.core.LogUtil;

public abstract class FrameworkService extends Service {

    private final AtomicBoolean create = new AtomicBoolean(false);

    @Override
    public final void onCreate() {
        super.onCreate();
        boolean start = Framework.get().isActive();
        LogUtil.getLogger().info("isActive() = {},this = {}", start, this.getClass().getSimpleName());
        if (!start) {
            stopSelf();
            return;
        }

        create.set(true);
        onServiceCreate();
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();

        LogUtil.getLogger().info("create.get() = {},this = {}", create.get(), this.getClass().getSimpleName());
        if (create.get()) {
            onServiceDestroy();
            create.set(false);
        }
    }

    @Override
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
