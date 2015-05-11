package cm.android.app.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import cm.android.app.core.MyManager;
import cm.android.app.test.server.TestManager;
import cm.android.sdk.PersistentService;
import cm.android.util.SystemUtil;

public class TestService1 extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private Handler mHandler = new Handler();

    @Override
    public void onServiceStart(Intent intent, int flags, int startId) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TestManager test = MyManager.getTestManager();
                test.count();

                startService(new Intent(TestService1.this, TestService2.class));
            }
        }, 2000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.error("ggg onCreate:process = " + SystemUtil.getCurProcessName(this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.error("ggg onDestroy");
    }
}
