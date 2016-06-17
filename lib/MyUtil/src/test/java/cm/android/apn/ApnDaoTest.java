package cm.android.apn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.Context;

import cm.android.util.BuildConfig;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class ApnDaoTest {

    @Test
    public void testAddComplexSuffix() throws Exception {
        Context context = RuntimeEnvironment.application;
        ApnDao mApnDao = new ApnDao(context.getContentResolver());
        String temp = mApnDao.addComplexSuffix("sdajhaj");
        boolean result = temp.equals("");
        assertEquals(false, result);
    }

    @Test
    public void testAddSuffix() throws Exception {
        String temp = ApnDao.addSuffix("ace");
        boolean rusult = temp.length() >= 3;
        assertEquals(rusult, true);
    }

    @Test
    public void testRemoveSuffix() throws Exception {
        String str = "kyaysdhkfadljfds";
        String temp = ApnDao.removeSuffix(str);
        boolean rusult = str.length() >= temp.length();
        assertEquals(rusult, true);
    }

    @Test
    public void testRemoveComplexSuffix() throws Exception {
        String str = "kyays;'p/.dhkfad/;//ljfds";
        String temp = ApnDao.removeComplexSuffix(str);
        boolean rusult = str.length() <= temp.length();
        assertEquals(rusult, true);
    }
}
