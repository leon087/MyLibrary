package cm.java.cmd;

import android.test.InstrumentationTestCase;

public class CmdExecuteTest extends InstrumentationTestCase {

    public void testExec() throws Exception {
        String temp = CmdExecute.exec("cat /proc/cpuinfo");
        boolean result = temp.equals("");
        assertEquals(result, false);
    }
}
