package cm.android.framework.component;

import android.content.Context;

import cm.android.framework.client.core.LogUtil;
import cm.android.framework.client.core.StateHelper;

public abstract class BaseBinderServer implements IBinderServer {

    protected Context context;

    private CoreReceiver coreReceiver = new CoreReceiver() {
        @Override
        public void restore() {
            startService();
        }
    };

    @Override
    public final void onCreate(Context context) {
        LogUtil.getLogger().info("onCreate");
        if (this.context != null) {
            return;
        }
        this.context = context;
        try {
            create(this.context);
            startService();
        } finally {
            coreReceiver.register(this.context);
        }
    }

    @Override
    public final void onDestroy() {
        LogUtil.getLogger().info("onDestroy");
        if (this.context == null) {
            return;
        }
        coreReceiver.unregister(context);
        try {
            stopService();
            destroy();
        } finally {
            this.context = null;
        }
    }

    @Override
    public boolean isActive(Context context) {
        return StateHelper.isActive(context);
    }

    protected abstract void create(Context context);

    protected abstract void destroy();

    protected abstract void startService();

    protected abstract void stopService();

}
