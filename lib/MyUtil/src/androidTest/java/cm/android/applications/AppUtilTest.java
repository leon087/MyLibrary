package cm.android.applications;


import android.content.pm.ApplicationInfo;
import android.test.InstrumentationTestCase;

public class AppUtilTest extends InstrumentationTestCase {

    public void testIsSystemApp() throws Exception {
        boolean result = (1 & ApplicationInfo.FLAG_SYSTEM) > 0;
        assertEquals(true, result);
    }

}