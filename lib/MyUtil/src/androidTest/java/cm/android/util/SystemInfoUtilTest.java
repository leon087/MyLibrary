package cm.android.util;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class SystemInfoUtilTest extends InstrumentationTestCase {

    public void testGetSystemProperty() throws Exception {
        String temp = SystemInfoUtil.getSystemProperty();
        boolean result = temp.contains(System.getProperty("java.vendor.url"));
        assertEquals(result, true);
    }

    public void testGetDisplayMetrics() throws Exception {
        Context context = getInstrumentation().getContext();
        String temp = SystemInfoUtil.getDisplayMetrics(context);
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetVersionInfo() throws Exception {
        String temp = SystemInfoUtil.getVersionInfo();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetCpuInfo() throws Exception {
        String temp = SystemInfoUtil.getCpuInfo();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetDiskInfo() throws Exception {
        String temp = SystemInfoUtil.getDiskInfo();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetDmesgInfo() throws Exception {
        String temp = SystemInfoUtil.getDmesgInfo();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetNetStatusInfo() throws Exception {
        String temp = SystemInfoUtil.getNetStatusInfo();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetMountInfo() throws Exception {
        String temp = SystemInfoUtil.getMountInfo();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetDumpsysMeminfo() throws Exception {
        Context context = getInstrumentation().getContext();
        String packagename = context.getPackageName();
        String temp = SystemInfoUtil.getDumpsysMeminfo(packagename);
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetDumpsysCpuinfo() throws Exception {
        String temp = SystemInfoUtil.getDumpsysCpuinfo();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetDumpsysBattery() throws Exception {
        String temp = SystemInfoUtil.getDumpsysBattery();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetMemoryInfo() throws Exception {
        Context context = getInstrumentation().getContext();
        String temp = SystemInfoUtil.getMemoryInfo(context);
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

    public void testGetProcessRunningInfo() throws Exception {
        String temp = SystemInfoUtil.getProcessRunningInfo();
        boolean result = temp.equals("");
        assertEquals(result, false);
    }

//    public void testGetMacAddress() throws Exception {
//        String temp = SystemInfoUtil.getMacAddress();
//        boolean result = temp.equals("");
//        assertEquals(result, false);
//    }
}
