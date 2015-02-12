package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import cm.android.framework.core.binder.ServiceBidnerImpl;
import cm.android.sdk.PersistentService;

public final class CoreService extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final ServiceBidnerImpl serviceBidner = new ServiceBidnerImpl();

    @Override
    public final IBinder onBind(Intent intent) {
        return serviceBidner;
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

    public final static boolean bind(Context context, ServiceConnection connection) {
        return context.getApplicationContext()
                .bindService(new Intent(context, CoreService.class), connection,
                        Context.BIND_AUTO_CREATE);
    }

    public final static void unBind(Context context, ServiceConnection connection) {
        context.getApplicationContext().unbindService(connection);
    }
}
