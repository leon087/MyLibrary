package cm.java.util;

import android.test.InstrumentationTestCase;

public class HexUtilTest extends InstrumentationTestCase {

    public void testEncodeAndDecode() throws Exception {
        byte[] input = "hello".getBytes();
        String temp = HexUtil.encode(input);
        byte[] result = HexUtil.decode(temp);
        assertEquals("hello", new String(result));
    }

    public void testEncodeUC() throws Exception {
        byte[] input = {111, 22, 33};
        String temp = HexUtil.encodeUC(input);
        assertEquals(temp, "6F1621");
    }
}
