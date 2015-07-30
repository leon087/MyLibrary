package cm.android.common.http.volley;

import android.test.InstrumentationTestCase;

import cm.android.common.am.ui.ApplicationsState;


public class VolleyUtilTest extends InstrumentationTestCase {

    public void testGetStringFromZip() throws Exception {
        byte[] data = {11, 10, 111};
        String result = VolleyUtil.getStringFromZip(data);
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

}
