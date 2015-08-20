package cm.android.framework.core.global;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.io.File;

public class WorkDirTest extends InstrumentationTestCase {

    public void testGetWorkDir() throws Exception {
        Context context = getInstrumentation().getContext();
        WorkDir.initWorkDir(context);
        File dataWorkDir = new File(context.getFilesDir(), "workpath");
        File[] temp = WorkDir.getWorkDir();
        assertEquals(dataWorkDir, temp[1]);
    }
}
