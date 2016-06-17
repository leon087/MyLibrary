package cm.android.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.test.InstrumentationTestCase;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class InstallationTest{

    @Test
    public void testId() throws Exception {
        Context context = TestUtil.getContext();
        String temp = Installation.id(context);
        boolean result = "".equals(temp);
        assertEquals(result, false);
    }
}
