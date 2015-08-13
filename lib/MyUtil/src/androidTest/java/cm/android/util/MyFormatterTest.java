package cm.android.util;


import android.test.InstrumentationTestCase;

public class MyFormatterTest extends InstrumentationTestCase {

    public void testFormatFileSize() throws Exception {
        //TODO ggg 不够详细
        String temp = MyFormatter.formatFileSize(12);

        boolean result = temp.equals("");
        assertEquals(result, false);
    }
}