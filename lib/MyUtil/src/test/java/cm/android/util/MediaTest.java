package cm.android.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class MediaTest {

    @Test
    public void testIsVideoOrAudio() throws Exception {
        boolean temp = Media.isNative("/jdah");
        assertEquals(temp, true);
    }

    @Test
    public void testIsNative() throws Exception {
        boolean temp = Media.isNative("/jdah");
        assertEquals(temp, true);
    }
}
