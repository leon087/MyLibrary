package cm.android.common.image;

import android.content.Context;
import android.test.InstrumentationTestCase;


public class ImageManagerTest extends InstrumentationTestCase {

    public void testGetOptions() throws Exception {
        boolean result = ImageManager.getOptions(1) != null;
        assertEquals(true, result);
    }

    public void testGetDefaultConfigurationBuilder() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean result = ImageManager.getDefaultConfigurationBuilder(context, "djh") != null;
        assertEquals(true, result);
    }
}
