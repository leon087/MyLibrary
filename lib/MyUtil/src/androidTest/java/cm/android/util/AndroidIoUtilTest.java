package cm.android.util;


import android.os.Environment;
import android.test.InstrumentationTestCase;

public class AndroidIoUtilTest extends InstrumentationTestCase {

    public void testIsDirectoryValid() throws Exception {//没有读写权限
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        boolean temp = true;
        temp = AndroidIoUtil.isDirectoryValid(path);
        assertEquals(temp, false);
    }

}
