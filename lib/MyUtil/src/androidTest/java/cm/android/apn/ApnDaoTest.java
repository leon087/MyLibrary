package cm.android.apn;

import android.test.InstrumentationTestCase;

public class ApnDaoTest extends InstrumentationTestCase {

//    public void testSetDataEnabled() throws Exception {
//        ApnDao mApnDao = new ApnDao(getInstrumentation().getContext().getContentResolver());
//        boolean temp = mApnDao.setDataEnabled(false);
//        assertEquals(temp, true);
//    }

//    public void testSetMmsEnabled() throws Exception {
//        ApnDao mApnDao = new ApnDao(getInstrumentation().getContext().getContentResolver());
//        boolean temp = mApnDao.setMmsEnabled(true);
//        assertEquals(temp, true);
//    }
//
//    public void testSetMmsEnabled1() throws Exception {
//        ApnDao mApnDao = new ApnDao(getInstrumentation().getContext().getContentResolver());
//        boolean temp = mApnDao.setMmsEnabled(false);
//        assertEquals(temp, true);
//    }

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
