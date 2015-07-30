package cm.android.common.am.ui;

import android.test.InstrumentationTestCase;


public class ApplicationsStateTest extends InstrumentationTestCase {

    public void testNormalize() throws Exception {
        String result = ApplicationsState.normalize("NUEFH");
        assertEquals(result, "nuefh");
        ApplicationsState.INTERNAL_SIZE_COMPARATOR.compare(null, null);
    }
}
