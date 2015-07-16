package cm.android.util;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import java.util.Properties;

public class AndroidUtilsTest extends InstrumentationTestCase {

    public void testIsEmpty1() throws Exception {
        Bundle bundle = new Bundle();
        boolean temp = AndroidUtils.isEmpty(bundle);
        assertEquals(temp, true);
    }

    public void testIsEmpty2() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putString("hjgde", "jhuds");
        boolean temp = AndroidUtils.isEmpty(bundle);
        assertEquals(temp, false);
    }

    public void testGetVersionCode() throws Exception {
        Context context = getInstrumentation().getContext();
        int temp = AndroidUtils.getVersionCode(context);
        if (temp == -1) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetSystemProperties() throws Exception {
        String temp = AndroidUtils.getSystemProperties("sss");
        if (temp.equals("")) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testLoadProperties() throws Exception {
        Context context = getInstrumentation().getContext();
        Properties temp = AndroidUtils.loadProperties(context, "sss");
        if (temp == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetDexCrc() throws Exception {
        Context context = getInstrumentation().getContext();
        long temp = AndroidUtils.getDexCrc(context);
        if (temp == 0) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
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
