package cm.android.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
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
        if (result == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetExternalStoragePublicDirectory() {
        File result = EnvironmentUtil.getExternalStoragePublicDirectory("123");
        if (result == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetExternalCacheDir() {
        Context context = getInstrumentation().getContext();
        File result = EnvironmentUtil.getExternalCacheDir(context);
        if (result == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetUsableSpace() {
        File file = EnvironmentUtil.getExternalStorageDirectory();
        long result = EnvironmentUtil.getUsableSpace(file);
        if (result == 0) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetAvailableSize() {
        File file = EnvironmentUtil.getExternalStorageDirectory();
        if (file == null || !file.exists()) {
            assertEquals(true, false);
        }
        try {
            StatFs statFs = new StatFs(file.getPath());
            long availableBlocks = statFs.getAvailableBlocks();// 可用存储块的数量
            long blockSize = statFs.getBlockSize();// 每块存储块的大小
            long availableSize = availableBlocks * blockSize;// 可用容量
            if (availableBlocks != 0) {
                assertEquals(true, true);
            } else {
                assertEquals(true, false);
            }
        } catch (Exception e) {
            assertEquals(true, false);
        }
    }

    public void testGetTotalSize() {
        long result = EnvironmentUtil.getTotalSize("1");
        if (result == -1) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetUsedSize() {
        long result = EnvironmentUtil.getUsedSize("1");
        if (result == 0) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
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
}
