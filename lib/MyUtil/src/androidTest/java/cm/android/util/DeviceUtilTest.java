package cm.android.util;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class DeviceUtilTest extends InstrumentationTestCase {

//    public void testGetDeviceFeatures() throws Exception {//android.permission.READ_PHONE_STATE.
//        Context context = getInstrumentation().getContext();
//        String temp = DeviceUtil.getDeviceFeatures(context);
//        boolean result = temp.length() > 0;
//        assertEquals(result, true);
//    }

    public void testGetSdkInt() throws Exception {
        int temp = DeviceUtil.getSdkInt();
        boolean result = temp > 0;
        assertEquals(result, true);
    }

    public void testGetBootloader() throws Exception {
        String temp = DeviceUtil.getBootloader();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetCpuAbi2() throws Exception {
        String temp = DeviceUtil.getCpuAbi2();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetHardware() throws Exception {
        String temp = DeviceUtil.getHardware();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetSerial() throws Exception {
        String temp = DeviceUtil.getSerial();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetAndroidId() throws Exception {
        Context context = getInstrumentation().getContext();
        String temp = DeviceUtil.getAndroidId(context);
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    // sim卡是否可读
    public void testIsSimReady() {
        Context context = getInstrumentation().getContext();//在有sim卡中的手机测试
        boolean temp = DeviceUtil.isSimReady(context);
        assertEquals(temp, true);
    }
}
