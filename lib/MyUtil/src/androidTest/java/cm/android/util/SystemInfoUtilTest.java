package cm.android.util;


import android.content.Context;
import android.test.InstrumentationTestCase;

public class SystemInfoUtilTest extends InstrumentationTestCase {

    public void testGetSystemProperty() throws Exception {
        String result = "";
        result = SystemInfoUtil.getSystemProperty();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetDisplayMetrics() throws Exception {
        Context context = getInstrumentation().getContext();
        String result = "";
        result = SystemInfoUtil.getDisplayMetrics(context);
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetVersionInfo() throws Exception {
        String result = "";
        result = SystemInfoUtil.getVersionInfo();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetCpuInfo() throws Exception {
        String result = "";
        result = SystemInfoUtil.getCpuInfo();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetDiskInfo() throws Exception {
        String result = "";
        result = SystemInfoUtil.getDiskInfo();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetDmesgInfo() throws Exception {
        String result = "";
        result = SystemInfoUtil.getDmesgInfo();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

//    public void testGetNetConfigInfo() throws Exception {
//        String result = SystemInfoUtil.getNetConfigInfo();
//        if (result.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

    public void testGetNetStatusInfo() throws Exception {
        String result = "";
        result = SystemInfoUtil.getNetStatusInfo();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetMountInfo() throws Exception {
        String result = "";
        result = SystemInfoUtil.getMountInfo();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetDumpsysMeminfo() throws Exception {
        Context context = getInstrumentation().getContext();
        String packagename = context.getPackageName();
        String result = "";
        result = SystemInfoUtil.getDumpsysMeminfo(packagename);
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetDumpsysCpuinfo() throws Exception {
        String result = "";
        result = SystemInfoUtil.getDumpsysCpuinfo();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetDumpsysBattery() throws Exception {
        String result = "";
        result = SystemInfoUtil.getDumpsysBattery();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetMemoryInfo() throws Exception {
        Context context = getInstrumentation().getContext();
        String result = "";
        result = SystemInfoUtil.getMemoryInfo(context);
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetProcessRunningInfo() throws Exception {
        String result = "";
        result = SystemInfoUtil.getProcessRunningInfo();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

//    public void testGetTelStatus() throws Exception {
//        String result = "";
//        Context context = getInstrumentation().getContext();
//        result = SystemInfoUtil.getTelStatus(context);//android.permission.READ_PHONE_STATE需要权限
//        if (result.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

    public void testGetMacAddress() throws Exception {
        String result = "";
        result = SystemInfoUtil.getMacAddress();
        if (result.equals("")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

}
