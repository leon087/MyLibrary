package cm.android.common.am.ui;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.test.InstrumentationTestCase;

public class ApplicationsStateTest extends InstrumentationTestCase {

    public void testNormalize() throws Exception {
        String result = ApplicationsState.normalize("NUEFH");
        assertEquals(result, "nuefh");
    }

    public void testEnsureIconLocked() throws Exception {
        Context context = getInstrumentation().getContext();
        ApplicationInfo applicationInfo = getInstrumentation().getContext().getApplicationInfo();
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        ApplicationsState.AppEntry appEntry = new ApplicationsState.AppEntry(context,
                applicationInfo, 23456l);
        boolean result = appEntry.ensureIconLocked(context, packageManager);
        assertEquals(result, true);
    }

    public void testGetNormalizedLabel() throws Exception {
        Context context = getInstrumentation().getContext();
        ApplicationInfo applicationInfo = getInstrumentation().getContext().getApplicationInfo();
        ApplicationsState.AppEntry appEntry = new ApplicationsState.AppEntry(context,
                applicationInfo, 23456l);
        String temp = appEntry.getNormalizedLabel();
        boolean result = temp.equals("");
        assertEquals(false, result);
    }
}
