package cm.android.framework.core.global;


import android.content.Context;
import android.test.InstrumentationTestCase;

import java.io.File;
import java.util.HashMap;

import cm.android.util.EnvironmentUtil;
import cm.java.util.IoUtil;
import cm.java.util.ObjectUtil;


public class WorkDirTest extends InstrumentationTestCase {

    public void testGetWorkDir() throws Exception {
        Context context = getInstrumentation().getContext();
        WorkDir.initWorkDir(context);
        File dataWorkDir = new File(context.getFilesDir(), "workpath");
        File[] temp = WorkDir.getWorkDir();
        assertEquals(dataWorkDir, temp[1]);
    }
}
