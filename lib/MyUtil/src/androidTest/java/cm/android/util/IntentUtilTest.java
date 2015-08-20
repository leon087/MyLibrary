package cm.android.util;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class IntentUtilTest extends InstrumentationTestCase {

    public void testLaunchApp() throws Exception {
        Context context = getInstrumentation().getContext();
        String packageName = context.getPackageName();
        boolean result = IntentUtil.launchApp(context, packageName);
        assertEquals(false, result);
    }

    public void testDeletePackage() throws Exception {
        Context context = getInstrumentation().getContext();
        String packageName = context.getPackageName();
        boolean result = IntentUtil.deletePackage(context, packageName);
        assertEquals(true, result);
    }
}
