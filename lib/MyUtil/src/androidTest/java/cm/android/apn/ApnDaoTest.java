package cm.android.apn;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class ApnDaoTest extends InstrumentationTestCase {

//    public void testIsDataEnabled() throws Exception {//android.permission.WRITE_APN_SETTINGS
//        Context context = getInstrumentation().getContext();
//        ApnDao mApnDao = new ApnDao(context.getContentResolver());
//        boolean result = mApnDao.isDataEnabled();
//        assertEquals(true, result);
//    }

//    public void testIsDataEnabled() throws Exception {//android.permission.WRITE_APN_SETTINGS.
//        Context context = getInstrumentation().getContext();
//        ApnDao mApnDao = new ApnDao(context.getContentResolver());
//        boolean result = mApnDao.isMmsEnabled();
//        assertEquals(true, result);
//    }

    public void testAddComplexSuffix() throws Exception {
        Context context = getInstrumentation().getContext();
        ApnDao mApnDao = new ApnDao(context.getContentResolver());
        String temp = mApnDao.addComplexSuffix("sdajhaj");
        boolean result = temp.equals("");
        assertEquals(false, result);
    }

    public void testAddSuffix() throws Exception {
        String temp = ApnDao.addSuffix("ace");
        boolean rusult = temp.length() >= 3;
        assertEquals(rusult, true);
    }

    public void testRemoveSuffix() throws Exception {
        String str = "kyaysdhkfadljfds";
        String temp = ApnDao.removeSuffix(str);
        boolean rusult = str.length() >= temp.length();
        assertEquals(rusult, true);
    }

    public void testRemoveComplexSuffix() throws Exception {
        String str = "kyays;'p/.dhkfad/;//ljfds";
        String temp = ApnDao.removeComplexSuffix(str);
        boolean rusult = str.length() <= temp.length();
        assertEquals(rusult, true);
    }
}
