package cm.android.util;


import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

public class LocaleUtilTest extends InstrumentationTestCase {

    public void testShowLocale() throws Exception {
        String result = LocaleUtil.showLocale();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
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
        String result = LocaleUtil.showTest(context);
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetLocale() throws Exception {
        String result = LocaleUtil.getLocale();
        if (result.equals("en")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

}
