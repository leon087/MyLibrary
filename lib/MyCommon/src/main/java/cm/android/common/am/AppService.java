package cm.android.common.am;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import cm.android.sdk.PersistentService;

public abstract class AppService extends PersistentService {

    public static final String ACTION_INIT_APP = "cm.android.intent.action.INIT_APP";

    private final IBinder mServiceBinder = new ServiceBinder();

    private final AppManager appManager = new AppManager();

    @Override
    public void onCreate() {
        appManager.init(this, configAppProcessor(), configAsynRequest());
//        appManager.initInstalledList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    @Override
    public void onServiceStart(Intent intent, int flags, int startId) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        if (UpdateAppManager.ACTION_REQUEST_UPDATE.equals(action)) {
            appManager.getUpdateAppManager().request();
        } else if (ACTION_INIT_APP.equals(action)) {
            appManager.initInstalledList();
        }
    }

    @Override
    public void onDestroy() {
        appManager.deInit();
    }

    public class ServiceBinder extends Binder {

        public AppManager getAppManager() {
            return appManager;
        }

        public UpdateAppManager getUpdateManager() {
            return appManager.getUpdateAppManager();
        }
    }

    protected abstract UpdateAppManager.IAsynRequest configAsynRequest();

    protected abstract AppManager.IAppProcessor configAppProcessor();
}
