package cm.android.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class AndroidUtilsTest {

    @Test
    public void testIsEmpty() throws Exception {
        Bundle bundle = new Bundle();
        boolean temp = AndroidUtils.isEmpty(bundle);
        assertEquals(temp, true);

        bundle.putString("hjgde", "jhuds");
        temp = AndroidUtils.isEmpty(bundle);
        assertEquals(temp, false);
    }

    @Test
    public void testGetVersionCode() throws Exception {
        Context context = TestUtil.getContext();
        int temp = AndroidUtils.getVersionCode(context);
        boolean result = temp != -1;
        assertEquals(result, true);
    }

    @Test
    public void testGetSystemProperties() throws Exception {
        String temp = AndroidUtils.getSystemProperties("sss");
        assertEquals(temp, "");
    }

    @Test
    public void testLoadProperties() throws Exception {
        Context context = TestUtil.getContext();
        Properties temp = AndroidUtils.loadProperties(context, "sss");
        boolean result = temp != null;
        assertEquals(true, result);

        int value = temp.size();
        assertEquals(0, value);
    }

    @Test
    public void testGetDexCrc() throws Exception {
        Context context = TestUtil.getContext();
        long temp = AndroidUtils.getDexCrc(context);
        assertEquals(temp != 0, false);
    }

    @Test
    public void testIsDebuggable() throws Exception {
        Context context = TestUtil.getContext();
        ApplicationInfo info = context.getApplicationInfo();
        boolean temp = AndroidUtils.isDebuggable(info);
        assertEquals(temp, false);
    }

//    public void testReboot() throws Exception {
//        Context context = getInstrumentation().getContext();
//        boolean temp = AndroidUtils.reboot(context);
//        assertEquals(temp, true);
//    }

    @Test
    public void testSetAdbEnabled() throws Exception {
        Context context = TestUtil.getContext();
        boolean temp = AndroidUtils.setAdbEnabled(context, 1);
        assertEquals(temp, true);
    }

    private Context getContext() {
        return RuntimeEnvironment.application;
    }
}
