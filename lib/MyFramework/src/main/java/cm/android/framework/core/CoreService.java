package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import cm.android.framework.core.binder.ServiceBinderImpl;
import cm.android.sdk.PersistentService;
import cm.android.util.EnvironmentUtil;

public final class CoreService extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final ServiceBinderImpl serviceBidner = new ServiceBinderImpl();

    private static final String TAG = "CoreService";

    @TargetApi(18)
    @Override
    public final IBinder onBind(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            logger.info("CoreService:onBind:initService");
            IBinder iBinder = bundle.getBinder(TAG);
            IServiceManager serviceManager = IServiceManager.Stub.asInterface(iBinder);
            serviceBidner.initService(serviceManager);
        }
        return serviceBidner;
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
        serviceBidner.initialize();
    }

    @Override
    public final void onDestroy() {
        logger.error("onDestroy");
        serviceBidner.release();
        super.onDestroy();
    }

    @TargetApi(18)
    public final static boolean bind(Context context, ServiceConnection connection,
            IServiceManager iServiceManager) {
        Intent intent = new Intent(context, CoreService.class);
        if (iServiceManager != null && EnvironmentUtil.SdkUtil.hasJellyBeanMr2()) {
            Bundle bundle = new Bundle();
            bundle.putBinder(TAG, iServiceManager.asBinder());
            intent.putExtras(bundle);
        }
        return context.getApplicationContext().bindService(intent, connection,
                Context.BIND_AUTO_CREATE);
    }

    public final static boolean bind(Context context, ServiceConnection connection) {
        return bind(context, connection, null);
    }

    public final static void unBind(Context context, ServiceConnection connection) {
        context.getApplicationContext().unbindService(connection);
    }
}
