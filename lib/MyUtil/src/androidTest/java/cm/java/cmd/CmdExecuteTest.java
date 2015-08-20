package cm.java.cmd;

import android.content.Context;
import android.test.InstrumentationTestCase;

import cm.android.util.EnvironmentUtil;

public class CmdExecuteTest extends InstrumentationTestCase {

    public void testRun() throws Exception {
        String[] cmd = {"jsdhdsja", "dsjhsd"};
        Context context = getInstrumentation().getContext();
        String workdirectory = EnvironmentUtil.getExternalDir(context).getPath();
        String temp = CmdExecute.run(cmd, workdirectory);
        boolean result = temp == null;
        assertEquals(true, result);
    }

    public void testExec() throws Exception {
        String temp = CmdExecute.exec("123");
        boolean result = temp == null;
        assertEquals(true, result);
    }
}
