package cm.android.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import cm.android.app.sample.ITestManager;
import cm.android.framework.core.InitListener;
import cm.android.framework.core.ServiceManager;
import cm.android.sdk.PersistentService;

public class TestService2 extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    @Override
    public void onStartService(Intent intent, int flags, int startId) {
        ServiceManager.start(new InitListener() {
            @Override
            public void initSucceed() {
                logger.error("ggg testService2 initSucceed");
                ITestManager test = ITestManager.Stub
                        .asInterface(ServiceManager.getService("Test"));
                try {
                    test.count();
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
