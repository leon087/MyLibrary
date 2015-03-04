package cm.android.framework.core;

import com.robotium.solo.Solo;

import android.test.InstrumentationTestCase;

public class BaseAppTest extends InstrumentationTestCase {

    private Solo solo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
    }


    public void testBaseApp() {
        assertEquals(true, true);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
