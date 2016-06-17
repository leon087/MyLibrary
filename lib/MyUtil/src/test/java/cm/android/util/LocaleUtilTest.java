package cm.android.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;

import java.util.Locale;

import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class LocaleUtilTest {

    @Test
    public void testShowLocale() throws Exception {
        String temp = LocaleUtil.showLocale();
        assertEquals(Locale.getDefault().toString().equals(""), false);
        boolean result = temp.contains(Locale.getDefault().toString());
        assertEquals(result, true);
    }

    @Test
    public void testShowTest() throws Exception {
        Context context = TestUtil.getContext();
        String temp = LocaleUtil.showTest(context);
        boolean result = temp
                .contains(context.getResources().getConfiguration().locale.getCountry());
        assertEquals(result, true);
    }

    @Test
    public void testGetLocale() throws Exception {
        String temp = LocaleUtil.getLocale();
        boolean result = temp.equals("en");
        assertEquals(result, false);
    }
}
