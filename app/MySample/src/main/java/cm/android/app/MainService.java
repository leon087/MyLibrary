package cm.android.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import cm.android.custom.MainApp;
import cm.android.framework.core.InitListener;
import cm.android.framework.core.ServiceManager;
import cm.android.sdk.PersistentService;

public class MainService extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    @Override
    public void onStartService(Intent intent, int flags, int startId) {
        MainApp.getApp().initApp(new InitListener() {
            @Override
            public void initSucceed() {
                logger.error("ggg initSucceed");
                MainApp.TestManager test = (MainApp.TestManager) ServiceManager
                        .getService("Test");
                try {
                    test.initialize();
                } catch (RemoteException e) {
                    LoggerFactory.getLogger("gggg").error(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.error("ggg onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.error("ggg onDestroy");
    }
}
