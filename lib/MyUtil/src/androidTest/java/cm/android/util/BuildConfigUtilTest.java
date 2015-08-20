package cm.android.util;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class BuildConfigUtilTest extends InstrumentationTestCase {

    public void testGetBuildConfigValue() throws Exception {
        Context context = getInstrumentation().getContext();
        Object temp = BuildConfigUtil.getBuildConfigValue(context, "dshds");
        boolean result = temp == null;
        assertEquals(result, true);
    }

    public void testIsDebug() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean temp = BuildConfigUtil.isDebug(context);
        assertEquals(temp, true);
    }
}
