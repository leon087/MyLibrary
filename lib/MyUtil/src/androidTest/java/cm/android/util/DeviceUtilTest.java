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
    public void testGetPair() throws Exception {
        String temp = "\n" + "123=321";
        String key = "123" == null ? "" : "123".trim();
        String value = "321" == null ? "" : "321".trim();
        String temp1 = "\n" + key + "=" + value;
        assertEquals(temp, temp1);
    }


    public void testGetSdkInt() throws Exception {
        int temp = DeviceUtil.getSdkInt();
        if (temp > 0) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetBootloader() throws Exception {
        String temp = DeviceUtil.getBootloader();
        if (temp.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetCpuAbi2() throws Exception {
        String temp = DeviceUtil.getCpuAbi2();
        if (temp.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetHardware() throws Exception {
        String temp = DeviceUtil.getHardware();
        if (temp.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetSerial() throws Exception {
        String temp = DeviceUtil.getSerial();
        if (temp.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetAndroidId() throws Exception {
        Context context = getInstrumentation().getContext();
        String temp = DeviceUtil.getAndroidId(context);
        if (temp.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetUUID() throws Exception {
        Context context = getInstrumentation().getContext();
        UUID temp = DeviceUtil.getUUID(context);
        if (temp == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
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
