package cm.java.cmd;

import android.test.InstrumentationTestCase;

public class ShellUtilTest extends InstrumentationTestCase {

    public void testCheckRootPermission() throws Exception {
        boolean result = ShellUtil.checkRootPermission();
        assertEquals(result, false);
    }

    public void testExec() throws Exception {
        String[] commands = {"123", "234"};
        ShellUtil.CommandResult temp = ShellUtil.exec(commands, false, true);
        boolean result = temp.result == -1;
        assertEquals(result, false);
    }
}
