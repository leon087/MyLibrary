package cm.android.util;


import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

public class LocaleUtilTest extends InstrumentationTestCase {

    public void testShowLocale() throws Exception {
        String temp = LocaleUtil.showLocale();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

//    public void testShowLocale2() throws Exception {
//        String result = LocaleUtil.showLocale2();//java.util.MissingResourceException: No 3-letter country code for locale: ar_001
//        if (result.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

    public void testShowTest() throws Exception {
        Context context = getInstrumentation().getContext();
        String temp = LocaleUtil.showTest(context);
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetLocale() throws Exception {
        String temp = LocaleUtil.getLocale();
        boolean result = temp.equals("en");
        assertEquals(result, false);
    }
}
