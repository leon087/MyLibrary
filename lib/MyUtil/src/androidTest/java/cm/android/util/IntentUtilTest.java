package cm.android.util;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.test.InstrumentationTestCase;

import cm.java.util.Utils;

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
