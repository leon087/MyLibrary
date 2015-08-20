package cm.java.util;

import android.test.InstrumentationTestCase;

public class Base64Test extends InstrumentationTestCase {

    public void testEncodeToStringAndDecode() throws Exception {
        byte[] input = {11, 22, 33};
        String temp = Base64
                .encodeToString(input, Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE);
        byte[] output = Base64.decode(temp, Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE);
        assertEquals(new String(input), new String(output));

        byte[] temp2 = android.util.Base64
                .encode(input, Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE);
        byte[] output2 = Base64.decode(temp2, Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE);
        assertEquals(new String(input), new String(output2));

    }
}
