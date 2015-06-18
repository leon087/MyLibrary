package cm.android.framework.core;

import android.content.Context;

public abstract class BaseServiceManager implements IServiceManager {

    private CoreReceiver coreReceiver = new CoreReceiver() {
        @Override
        public void restore() {
            startService();
        }
    };

    @Override
    public final void onCreate(Context context) {
        create(context);
        startService();
        coreReceiver.register(context);
    }

    @Override
    public final void onDestroy() {
        coreReceiver.unregister();
        stopService();
        destroy();
    }

    protected abstract void create(Context context);

    protected abstract void destroy();

    protected abstract void startService();

    protected abstract void stopService();

}
