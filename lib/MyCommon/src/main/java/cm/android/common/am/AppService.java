package cm.android.common.am;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import cm.android.sdk.PersistentService;

public abstract class AppService extends PersistentService {

    public static final String ACTION_ADD_APP = "android.app.action_add_app";

    public static final String ACTION_REMOVE_APP = "android.app.action_remove_app";

    public static final String PACKAGE_NAME = "packageName";

    private IBinder mServiceBinder = new ServiceBinder();

    private AppManager appManager = new AppManager();

    @Override
    public void onCreate() {
        super.onCreate();
        appManager.init(this);
        appManager.initInstalledList();
        appManager.getUpdateAppManager().config(configAsynRequest());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    @Override
    public void onServiceStart(Intent intent, int flags, int startId) {
    }

    @Override
    public void onDestroy() {
        appManager.deInit();
        super.onDestroy();
    }

    public class ServiceBinder extends Binder {

        public AppManager getService() {
            return appManager;
        }
    }

    protected abstract UpdateAppManager.IAsynRequest configAsynRequest();
}
