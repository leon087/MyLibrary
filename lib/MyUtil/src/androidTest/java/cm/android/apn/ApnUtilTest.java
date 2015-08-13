package cm.android.apn;


import android.content.Context;
import android.telephony.TelephonyManager;
import android.test.InstrumentationTestCase;

public class ApnUtilTest extends InstrumentationTestCase {

    public void testAddAPN() throws Exception {//在有sim卡的手机上测试
        Context context = getInstrumentation().getContext();
        ApnModel testModel = new ApnModel();
        int result = ApnUtil.addAPN(context, testModel);
        assertEquals(result, -1);
    }

    public void testSetApn() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean temp = ApnUtil.setApn(context, 0);
        assertEquals(temp, false);
    }

    public void testQueryByApn() throws Exception {
        Context context = getInstrumentation().getContext();
        int temp = ApnUtil.queryByApn(context, "ajsd");
        assertEquals(temp, -1);
    }
}
