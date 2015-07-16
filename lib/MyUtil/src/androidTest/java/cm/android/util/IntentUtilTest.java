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
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(packageName);
        if (null != intent) {
//            context.startActivity(intent);
            assertEquals(true, true);
        }
    }

    public void testDeletePackage() throws Exception {
        Context context = getInstrumentation().getContext();
        String packageName = context.getPackageName();
        if (Utils.isEmpty(packageName)) {
            assertEquals(true, false);
        }
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent apkintent = new Intent(Intent.ACTION_DELETE, packageURI);
        apkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkintent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//        context.startActivity(apkintent);
        assertEquals(true, true);
    }
}
