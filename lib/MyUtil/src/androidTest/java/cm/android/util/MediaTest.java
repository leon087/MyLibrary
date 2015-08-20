package cm.android.util;

import android.test.InstrumentationTestCase;

public class MediaTest extends InstrumentationTestCase {

    public void testIsVideoOrAudio() throws Exception {
        boolean temp = Media.isNative("/jdah");
        assertEquals(temp, true);
    }

    public void testIsNative() throws Exception {
        boolean temp = Media.isNative("/jdah");
        assertEquals(temp, true);
    }
}
