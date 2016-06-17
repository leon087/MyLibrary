package cm.android.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.res.AssetManager;
import android.os.Environment;
import android.test.InstrumentationTestCase;

import java.io.File;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class AndroidIoUtilTest {

    @Test
    public void testIsDirectoryValid() throws Exception {//没有权限
        String path = EnvironmentUtil.getExternalStorageDirectory().getPath();
        boolean result = AndroidIoUtil.isDirectoryValid(path);
        assertEquals(result, false);
    }

    @Test
    public void testCopyAssetFile() throws Exception {
        AssetManager assetManager = RuntimeEnvironment.application.getAssets();
        String fileName = EnvironmentUtil.getExternalStorageDirectory().getPath();
        File destFile = Environment.getDataDirectory();
        boolean result = AndroidIoUtil.copyAssetFile(assetManager, fileName, destFile);
        assertEquals(result, false);
    }
}
