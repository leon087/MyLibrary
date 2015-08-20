package cm.android.common.am.ui_old;

import android.test.InstrumentationTestCase;

public class ApplicationsStateTest extends InstrumentationTestCase {

    public void testNormalize() throws Exception {
        String result = ApplicationsState.normalize("NUEFH");
        assertEquals(result, "nuefh");
    }
}
