package cm.android.util;

import android.test.InstrumentationTestCase;

import java.io.File;

public class EnvironmentUtilTest extends InstrumentationTestCase {

    public void testHasEnoughSpace() {
        File file = EnvironmentUtil.getExternalStorageDirectory();
        boolean result = EnvironmentUtil.hasEnoughSpace(file);

        assertEquals(true, result);
    }
}
