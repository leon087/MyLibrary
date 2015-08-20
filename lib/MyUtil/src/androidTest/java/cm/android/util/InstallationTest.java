package cm.android.util;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class InstallationTest extends InstrumentationTestCase {

    public void testId() throws Exception {
        Context context = getInstrumentation().getContext();
        String temp = Installation.id(context);
        boolean result = temp.equals("");
        assertEquals(result, false);
    }
}
