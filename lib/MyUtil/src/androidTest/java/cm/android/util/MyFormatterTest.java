package cm.android.util;

import android.test.InstrumentationTestCase;

public class MyFormatterTest extends InstrumentationTestCase {

    public void testFormatFileSize() throws Exception {
        String temp = MyFormatter.formatFileSize(1024);
        assertEquals(temp, "1.00KB");
    }

    public void testFormatDate() throws Exception {
        String temp = MyFormatter.formatDate(1439520017722l);
        assertEquals(temp, "2015-08-14 10:40:17");

        String temp2 = MyFormatter.formatDate2(1439520017722l);
        assertEquals(temp2, "20150814104017");

        String temp3 = MyFormatter.formatDate("yyyy-MM-dd HH:mm:ss.SSS", 1439520017722l);
        assertEquals(temp3, "2015-08-14 10:40:17");
    }
}