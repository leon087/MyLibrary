package cm.android.util;

import android.content.Context;
import android.os.Environment;
import android.test.InstrumentationTestCase;

import java.io.File;

public class EnvironmentUtilTest extends InstrumentationTestCase {

    public void testHasEnoughSpace() {
        File file = EnvironmentUtil.getExternalStorageDirectory();
        boolean result = EnvironmentUtil.hasEnoughSpace(file);
        assertEquals(true, result);
    }

    public void testIsExternalStorageMounted() {
        boolean result = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
        assertEquals(true, result);
    }

    public void testIsExternalStorageRemovable() {
        boolean result;
        if (EnvironmentUtil.SdkUtil.hasGingerbread()) {
            result = Environment.isExternalStorageRemovable();
        } else {
            result = true;
        }
        assertEquals(false, result);
    }

    public void testIsExternalStorageReadable() {
        boolean result = EnvironmentUtil.isExternalStorageReadable();
        assertEquals(true, result);
    }

    public void testIsExternalStorageWritable() {
        boolean result = EnvironmentUtil.isExternalStorageWritable();
        assertEquals(true, result);
    }

    public void testGetExternalStorageDirectory() {
        File result = EnvironmentUtil.getExternalStorageDirectory();
        assertEquals(result, Environment.getExternalStorageDirectory());
    }

    public void testGetExternalStoragePublicDirectory() {
        File result = EnvironmentUtil.getExternalStoragePublicDirectory("123");
        assertEquals(result, Environment.getExternalStoragePublicDirectory("123"));
    }

    public void testGetExternalCacheDir() {
        Context context = getInstrumentation().getContext();
        File result = EnvironmentUtil.getExternalCacheDir(context);
        assertEquals(result, context.getExternalCacheDir());
    }

    public void testGetUsableSpace() {
        File file = EnvironmentUtil.getExternalStorageDirectory();
        long result = EnvironmentUtil.getUsableSpace(file);
        assertEquals(result, file.getUsableSpace());
    }

    public void testGetTotalSpace() {
        long result = EnvironmentUtil.getTotalSpace(new File("1"));
        boolean temp = result == -1;
        assertEquals(true, temp);
    }

    public void testGetUsedSize() {
        long result = EnvironmentUtil.getUsedSize(new File("1"));
        boolean temp = result == 0;
        assertEquals(true, temp);
    }

    public void testIsTablet() {
        Context context = getInstrumentation().getContext();
        boolean result = EnvironmentUtil.isTablet(context);
        assertEquals(false, result);
    }

    public void testHasExternalStoragePermission() {
        Context context = getInstrumentation().getContext();
        boolean result = EnvironmentUtil.hasExternalStoragePermission(context);
        assertEquals(false, result);
    }

    public void testHasPermission() {
        Context context = getInstrumentation().getContext();
        String permission = "android.permission.READ_PHONE_STATE";
        boolean result = EnvironmentUtil.hasPermission(context, permission);
        assertEquals(false, result);
    }
}
