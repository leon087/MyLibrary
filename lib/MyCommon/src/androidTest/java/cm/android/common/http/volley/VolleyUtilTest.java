package cm.android.common.http.volley;

import android.test.InstrumentationTestCase;

import java.util.HashMap;
import java.util.Map;

public class VolleyUtilTest extends InstrumentationTestCase {

    public void testParseBody() throws Exception {
        Map<String, String> body = new HashMap<String, String>();
        body.put("123", "321");
        String temp = VolleyUtil.parseBody(body);
        assertEquals(temp, "{\"123\":\"321\"}");
    }

    public void testGetStringFromZip() throws Exception {
        byte[] data = {11, 10, 111};
        String temp = VolleyUtil.getStringFromZip(data);
        boolean result = temp.equals("");
        assertEquals(result, "");
    }

}
