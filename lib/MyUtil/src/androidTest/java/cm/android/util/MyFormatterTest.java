package cm.android.util;


import android.os.Bundle;
import android.test.InstrumentationTestCase;

public class MyFormatterTest extends InstrumentationTestCase {

    public void testFormatFileSize() throws Exception {
        String result = MyFormatter.formatFileSize(12);
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }
}