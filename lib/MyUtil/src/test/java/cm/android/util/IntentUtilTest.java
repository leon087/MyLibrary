package cm.android.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class IntentUtilTest {

    @Test
    public void testLaunchApp() throws Exception {
        Context context = TestUtil.getContext();
        String packageName = context.getPackageName();
        boolean result = IntentUtil.launchApp(context, packageName);
        assertEquals(false, result);
    }

    @Test
    public void testDeletePackage() throws Exception {
        Context context = TestUtil.getContext();
        String packageName = context.getPackageName();
        boolean result = IntentUtil.deletePackage(context, packageName);
        assertEquals(true, result);
    }
}
