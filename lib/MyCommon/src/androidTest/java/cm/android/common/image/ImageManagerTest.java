package cm.android.common.image;

import android.content.Context;
import android.test.InstrumentationTestCase;


public class ImageManagerTest extends InstrumentationTestCase {

    public void testGetOptions() throws Exception {
        if (ImageManager.getOptions(1) == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetDefaultConfigurationBuilder() throws Exception {
        Context context = getInstrumentation().getContext();
        if (ImageManager.getDefaultConfigurationBuilder(context, "djh") == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }


}
