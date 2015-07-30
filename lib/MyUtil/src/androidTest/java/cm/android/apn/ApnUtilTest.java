package cm.android.apn;


import android.content.Context;
import android.telephony.TelephonyManager;
import android.test.InstrumentationTestCase;

public class ApnUtilTest extends InstrumentationTestCase {

    public void testGetSimOperator() throws Exception {
        Context context = getInstrumentation().getContext();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String SimOperator = tm.getSimOperator();
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        int simState = mTelephonyManager.getSimState();
        if (0 == simState) {
            if (SimOperator.equals("")) {
                assertEquals(true, true);
            } else {
                assertEquals(true, false);
            }
        } else {
            if (SimOperator.equals("")) {
                assertEquals(true, false);
            } else {
                assertEquals(true, true);
            }
        }
    }

    public void testSetApn() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean temp = ApnUtil.setApn(context, 0);
        assertEquals(temp, false);
    }

    public void testQueryByApn() throws Exception {
        Context context = getInstrumentation().getContext();
        int temp = ApnUtil.queryByApn(context, "ajsd");
        if (temp == -1) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }
}
