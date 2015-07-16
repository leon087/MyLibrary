package cm.android.util;


import android.test.InstrumentationTestCase;

import java.util.regex.Pattern;

public class MediaTest extends InstrumentationTestCase {

    public void testIsNative() throws Exception {
        boolean temp = Media.isNative("/jdah");
        assertEquals(temp, true);
    }
}
