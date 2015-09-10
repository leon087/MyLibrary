package cm.android.app.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import cm.android.app.core.MainApp;
import cm.android.app.core.MyManager;
import cm.android.app.test.alarm.TestReceiver;
import cm.android.app.test.alarm.TestReceiver2;
import cm.android.framework.ext.alarm.TimerManager;
import cm.android.sdk.PersistentService;

public class TestService2 extends PersistentService {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private Handler mHandler = new Handler();

    @Override
    public void onServiceStart(Intent intent, int flags, int startId) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logger.error("ggg TestService2");
                TimerManager timerTaskManager = MyManager.getTimerManager();
                timerTaskManager.register(TestReceiver.ACTION, 5 * 1000, 0);
                timerTaskManager.register(TestReceiver2.ACTION, 10 * 1000, 0);

                testReceiver.register(MainApp.getApp());
                testReceiver2.register(MainApp.getApp());

                timerTaskManager.unregister(TestReceiver.ACTION);
            }
        }, 1000);
    }

    private TestReceiver testReceiver = new TestReceiver();

    private TestReceiver2 testReceiver2 = new TestReceiver2();

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
