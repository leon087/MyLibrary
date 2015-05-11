package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import cm.android.framework.core.binder.ServiceBinderImpl;
import cm.android.sdk.PersistentService;

public abstract class AbstractCoreService extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final ServiceBinderImpl serviceBinder = new ServiceBinderImpl();

    private static final String TAG = "CoreService";

    private static final String APPLICATION_ID = "applicationId";

    @TargetApi(18)
    @Override
    public final IBinder onBind(Intent intent) {
        logger.info("CoreService:onBind:initService");
        return serviceBinder;
    }

    @Override
    public final boolean onUnbind(Intent intent) {
        logger.info("CoreService:onUnbind:intent = " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public final void onServiceStart(Intent intent, int flags, int startId) {

    }

    @Override
    public final void onCreate() {
        super.onCreate();
        serviceBinder.initialize(this);
//        serviceBinder.initService(initService());
        logger.info("CoreService:onCreate");
    }

    @Override
    public final void onDestroy() {
        logger.error("CoreService:onDestroy");
        serviceBinder.release();
        super.onDestroy();
    }

    @TargetApi(18)
    public final static boolean bind(Context context,
            ServiceConnection connection, Class<? extends AbstractCoreService> klass) {
        Intent intent = new Intent(context, klass);
        return context.getApplicationContext().bindService(intent, connection,
                Context.BIND_AUTO_CREATE);
    }

    public final static void unBind(Context context, ServiceConnection connection) {
        context.getApplicationContext().unbindService(connection);
    }

    protected abstract IServiceManager initService();
}
