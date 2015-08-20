package cm.android.util;

import android.content.res.AssetManager;
import android.os.Environment;
import android.test.InstrumentationTestCase;

import java.io.File;

public class AndroidIoUtilTest extends InstrumentationTestCase {

    public void testIsDirectoryValid() throws Exception {//没有权限
        String path = EnvironmentUtil.getExternalStorageDirectory().getPath();
        boolean result = AndroidIoUtil.isDirectoryValid(path);
        assertEquals(result, false);
    }

    public void testCopyAssetFile() throws Exception {
        AssetManager assetManager = getInstrumentation().getContext().getAssets();
        String fileName = EnvironmentUtil.getExternalStorageDirectory().getPath();
        File destFile = Environment.getDataDirectory();
        boolean result = AndroidIoUtil.copyAssetFile(assetManager, fileName, destFile);
        assertEquals(result, false);
    }
}
