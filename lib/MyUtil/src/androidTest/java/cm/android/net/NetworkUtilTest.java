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
}
