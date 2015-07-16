package cm.android.util;

import android.content.Context;
import android.test.InstrumentationTestCase;


public class BuildConfigUtilTest extends InstrumentationTestCase {

//    public void testGetBuildConfigValue() throws Exception {
//        Context context = getInstrumentation().getContext();
//        Object temp = BuildConfigUtil.getBuildConfigValue(context, "sfr");
//        if (temp == null) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

    public void testIsDebug() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean temp = BuildConfigUtil.isDebug(context);
        assertEquals(temp, true);
    }
}
