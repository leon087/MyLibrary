package cm.android.sdk;


import android.content.Context;
import android.os.PowerManager;
import android.test.InstrumentationTestCase;

public class WakeLockUtilTest extends InstrumentationTestCase {

    public void testNewWakeLock() throws Exception {
        Context context = getInstrumentation().getContext();
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "dh");
        boolean result = wakeLock == null;
        assertEquals(result, false);
    }

//    public void testAcquire() throws Exception {//需要android.permission.WAKE_LOCK 权限
//        Context context = getInstrumentation().getContext();
//        PowerManager.WakeLock wakeLock = WakeLockUtil.acquire(context, "framework", 1000);
//        if (wakeLock == null) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

}
