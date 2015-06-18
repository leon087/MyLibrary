package cm.android.common.am;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class AppBindHolder {

    private static final Logger logger = LoggerFactory.getLogger("am");

    private boolean isBind = false;

    private AppManager appService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppService.ServiceBinder binder = (AppService.ServiceBinder) service;
            appService = binder.getAppManager();
            serviceListener.onServiceConnected();
            // appService.notifyAppListener();
            // appService.getUpdateAppManager().notifyUpdateAppListener();
            logger.info("name = " + name);
        }

        public void onServiceDisconnected(ComponentName name) {
            logger.info("name = " + name);
            appService = null;
        }
    };

    private ServiceListener serviceListener;

    public AppBindHolder(ServiceListener serviceListener) {
        this.serviceListener = serviceListener;
    }

    public void bindService(Activity activity, Class serviceClass) {
        logger.info("bindService");
        Intent intent = new Intent(activity, serviceClass);
        activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        isBind = true;
    }

    public void unbindService(Activity activity) {
        if (isBind) {
            activity.unbindService(mConnection);
            isBind = false;
        }
    }

    public AppManager getAppManager() {
        return appService;
    }

    public UpdateAppManager getUpdateManager() {
        return appService.getUpdateAppManager();
    }

    public static interface ServiceListener {

        void onServiceConnected();
    }

}
