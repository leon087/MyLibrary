package cm.android.util;


import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.UUID;

import cm.android.apn.ApnDao;

public class DeviceUtilTest extends InstrumentationTestCase {

    //    public void testGetDeviceFeatures() throws Exception {//android.permission.READ_PHONE_STATE需要权限
//        Context context = getInstrumentation().getContext();
//        String temp = DeviceUtil.getDeviceFeatures(context);
//        if (temp.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
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

    public void testGetUUID() throws Exception {
        Context context = getInstrumentation().getContext();
        UUID temp = DeviceUtil.getUUID(context);
        boolean result = temp != null;
        assertEquals(result, true);
    }

//    public void testGetMacAddress() throws Exception {
//        Context context = getInstrumentation().getContext();
//        String temp = DeviceUtil.getMacAddress(context);
//        if (temp.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

//    public void testGetIpAddress() throws Exception {
//        String temp = DeviceUtil.getIpAddress();
//        if (temp.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

//    public void testGetIMEI() throws Exception {
//        Context context = getInstrumentation().getContext();
//        String temp = DeviceUtil.getIMEI(context);
//        if (temp.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }


}
