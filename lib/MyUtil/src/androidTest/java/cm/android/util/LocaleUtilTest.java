package cm.android.util;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.Locale;

public class LocaleUtilTest extends InstrumentationTestCase {

    public void testShowLocale() throws Exception {
        String temp = LocaleUtil.showLocale();
        assertEquals(Locale.getDefault().toString().equals(""), false);
        boolean result = temp.contains(Locale.getDefault().toString());
        assertEquals(result, true);
    }

    public void testShowTest() throws Exception {
        Context context = getInstrumentation().getContext();
        String temp = LocaleUtil.showTest(context);
        boolean result = temp
                .contains(context.getResources().getConfiguration().locale.getCountry());
        assertEquals(result, true);
    }

    public void testGetLocale() throws Exception {
        String temp = LocaleUtil.getLocale();
        boolean result = temp.equals("en");
        assertEquals(result, false);
    }
}
