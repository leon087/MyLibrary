package cm.android.net;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class NetworkUtilTest extends InstrumentationTestCase {

    public void testGetNetWorkType() throws Exception {
        Context context = getInstrumentation().getContext();

        int temp = NetworkUtil.getNetWorkType(context);
        boolean result = temp != 0;

        assertEquals(true, result);
    }

    // sim卡是否可读
    public void testIsSimReady() {
        Context context = getInstrumentation().getContext();//在有sim卡中的手机测试
        boolean temp = NetworkUtil.isSimReady(context);
        assertEquals(temp, true);
    }
}
