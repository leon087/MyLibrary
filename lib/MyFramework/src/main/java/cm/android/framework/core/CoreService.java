package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import cm.android.framework.core.binder.ServiceBinderImpl;
import cm.android.sdk.PersistentService;

public final class CoreService extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final ServiceBinderImpl serviceBinder = new ServiceBinderImpl();

    private static final String ACTION_BIND = "cm.android.framework.intent.action.BIND_CORESERVICE";

    private static final String TAG_ACTION = "actionBind";

    private static final String TAG_SERVICE_MANAGER = "SERVICE_MANAGER";

    private static final IServiceBinder.Stub emptyServiceBinder = new IServiceBinder.Stub() {

        @Override
        public void create() throws RemoteException {

        }

        @Override
        public void destroy() throws RemoteException {

        }

        @Override
        public IBinder getService(String name) throws RemoteException {
            return null;
        }

        @Override
        public void addService(String name, IBinder binder) throws RemoteException {

        }

        public boolean isInit() throws RemoteException {
            return false;
        }
    };

    @TargetApi(18)
    @Override
    public final IBinder onBind(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            logger.error("bundle = null,intent = " + intent);
            return emptyServiceBinder;
        }

        String action = bundle.getString(TAG_ACTION);
        if (!ACTION_BIND.equals(action)) {
            logger.error("intent = {}", intent);
            return emptyServiceBinder;
        }

        logger.info("CoreService:onBind:initService:intent = " + intent);
        String serviceManagerName = bundle.getString(TAG_SERVICE_MANAGER);
        serviceBinder.initService(serviceManagerName);
        return serviceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        logger.info("CoreService:onUnbind:intent = " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onServiceStart(Intent intent, int flags, int startId) {

    }

    @Override
    public final void onCreate() {
        super.onCreate();
        serviceBinder.initialize(this);
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
            ServiceConnection connection, String serviceManagerName) {
        Intent intent = new Intent(context, CoreService.class);
        Bundle bundle = new Bundle();
        bundle.putString(TAG_SERVICE_MANAGER, serviceManagerName);
        bundle.putString(TAG_ACTION, ACTION_BIND);
        intent.putExtras(bundle);
        return context.getApplicationContext().bindService(intent, connection,
                Context.BIND_AUTO_CREATE);
    }

    public final static void unBind(Context context, ServiceConnection connection) {
        context.getApplicationContext().unbindService(connection);
    }
}
