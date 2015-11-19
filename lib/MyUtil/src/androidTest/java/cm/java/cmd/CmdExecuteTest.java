package cm.java.cmd;

import android.test.InstrumentationTestCase;

public class CmdExecuteTest extends InstrumentationTestCase {

    public void testExec() throws Exception {
        String[] args = new String[]{
                "cat", "/proc/cpuinfo"
        };
        String temp = CmdExecute.exec(args);
        boolean result = "".equals(temp);
        assertEquals(result, false);
    }
}
