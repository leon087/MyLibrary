package cm.android.net;

import android.content.Context;
import android.test.InstrumentationTestCase;


public class NetworkUtilTest extends InstrumentationTestCase {

    public void testGetNetWorkType() throws Exception {
        Context context = getInstrumentation().getContext();
        int temp = 0;
        temp = NetworkUtil.getNetWorkType(context);
        boolean result = temp != 0;
        assertEquals(true, result);
    }

    // sim卡是否可读
    public void testIsSimReady() {
        Context context = getInstrumentation().getContext();//在有sim卡中的手机测试
        boolean temp;
        temp = NetworkUtil.isSimReady(context);
        assertEquals(temp, true);
    }

//    public void testIsActiveNetworkMetered() {//需要 android.permission.ACCESS_NETWORK_STATE.
//        Context context = getInstrumentation().getContext();
//        boolean temp;
//        temp = NetworkUtil.isActiveNetworkMetered(context);
//        assertEquals(temp, true);
//    }

//    public void testIsConnected() {//需要 android.permission.ACCESS_NETWORK_STATE.
//        Context context = getInstrumentation().getContext();//在连接的时候测试
//        boolean temp;
//        temp = NetworkUtil.isConnected(context);
//        assertEquals(temp, true);
//    }

}
