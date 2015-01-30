package cm.android.app.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.IBinder;

import cm.android.app.core.MyManager;
import cm.android.app.test.server.TestManager;
import cm.android.framework.core.ServiceManager;
import cm.android.sdk.PersistentService;

public class TestService1 extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    @Override
    public void onStartService(Intent intent, int flags, int startId) {
        ServiceManager.start(new ServiceManager.InitListener() {
            @Override
            public void initSucceed() {
                logger.error("ggg testService1 initSucceed");
                TestManager test = MyManager.getTestManager();
                test.count();

                startService(new Intent(TestService1.this, TestService2.class));
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
