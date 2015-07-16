package cm.android.applications;


import android.content.Context;
import android.test.InstrumentationTestCase;

public class InterestingConfigChangesTest extends InstrumentationTestCase {

    public void testApplyNewConfig() throws Exception {
        final InterestingConfigChanges mInterestingConfigChanges = new InterestingConfigChanges();
        Context context = getInstrumentation().getContext();
        boolean tem = mInterestingConfigChanges.applyNewConfig(context.getResources());
        assertEquals(tem,true);
    }

}
