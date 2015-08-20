package cm.android.framework.core;

import android.content.Context;

public abstract class BaseServiceManager implements IServiceManager {

    private Context context;

    private CoreReceiver coreReceiver = new CoreReceiver() {
        @Override
        public void restore() {
            startService();
        }
    };

    @Override
    public final void onCreate(Context context) {
        this.context = context;
        create(this.context);
        startService();
        coreReceiver.register(this.context);
    }

    @Override
    public final void onDestroy() {
        coreReceiver.unregister(context);
        stopService();
        destroy();
        this.context = null;
    }

    protected abstract void create(Context context);

    protected abstract void destroy();

    protected abstract void startService();

    protected abstract void stopService();

}
