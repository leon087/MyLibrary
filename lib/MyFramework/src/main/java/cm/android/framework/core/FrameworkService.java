package cm.android.framework.core;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.sdk.PersistentService;

public abstract class FrameworkService extends PersistentService {

    private final AtomicBoolean create = new AtomicBoolean(false);

    @Override
    public final void onCreate() {
        super.onCreate();
        if (!ServiceManager.isBindService()) {
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

    protected abstract void onServiceCreate();

    protected abstract void onServiceDestroy();

}
