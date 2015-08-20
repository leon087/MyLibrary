package cm.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import java.util.Map;
import java.util.Properties;

public class AndroidUtilsTest extends InstrumentationTestCase {

    public void testIsEmpty() throws Exception {
        Bundle bundle = new Bundle();
        boolean temp = AndroidUtils.isEmpty(bundle);
        assertEquals(temp, true);

        bundle.putString("hjgde", "jhuds");
        temp = AndroidUtils.isEmpty(bundle);
        assertEquals(temp, false);
    }

    public void testGetStubFile() throws Exception {
        Context context = getInstrumentation().getContext();
        Map<String, Object> temp = AndroidUtils.getStubFile(context, "sss");
        boolean result = temp.isEmpty();
        assertEquals(result, true);
    }

    public void testGetVersionCode() throws Exception {
        Context context = getInstrumentation().getContext();
        int temp = AndroidUtils.getVersionCode(context);
        boolean result = temp != -1;
        assertEquals(result, true);
    }

    public void testGetSystemProperties() throws Exception {
        String temp = AndroidUtils.getSystemProperties("sss");
        assertEquals(temp, "");
    }

    public void testLoadProperties() throws Exception {
        Context context = getInstrumentation().getContext();
        Properties temp = AndroidUtils.loadProperties(context, "sss");
        boolean result = temp != null;
        assertEquals(true, result);

        int value = temp.size();
        assertEquals(0, value);
    }

    public void testGetDexCrc() throws Exception {
        Context context = getInstrumentation().getContext();
        long temp = AndroidUtils.getDexCrc(context);
        assertEquals(temp != 0, true);
    }

    public void testIsDebuggable() throws Exception {
        Context context = getInstrumentation().getContext();
        ApplicationInfo info = context.getApplicationInfo();
        boolean temp = AndroidUtils.isDebuggable(info);
        assertEquals(temp, true);
    }

//    public void testReboot() throws Exception {
//        Context context = getInstrumentation().getContext();
//        boolean temp = AndroidUtils.reboot(context);
//        assertEquals(temp, true);
//    }

    public void testSetAdbEnabled() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean temp = AndroidUtils.setAdbEnabled(context, 1);
        assertEquals(temp, false);
    }
}
