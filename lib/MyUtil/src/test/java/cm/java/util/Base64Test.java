package cm.java.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import cm.android.util.BuildConfig;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class Base64Test {

    @Test
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
