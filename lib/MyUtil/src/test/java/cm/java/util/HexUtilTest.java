package cm.java.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HexUtilTest {
    @Test
    public void testEncodeAndDecode() throws Exception {
        byte[] input = "hello".getBytes();
        String temp = HexUtil.encode(input);
        byte[] result = HexUtil.decode(temp);
        assertEquals("hello", new String(result));
    }

    @Test
    public void testEncodeUC() throws Exception {
        byte[] input = {111, 22, 33};
        String temp = HexUtil.encodeUC(input);
        assertEquals(temp, "6F1621");
    }
}
