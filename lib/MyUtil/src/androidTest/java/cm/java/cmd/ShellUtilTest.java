package cm.java.cmd;

import android.test.InstrumentationTestCase;

public class ShellUtilTest extends InstrumentationTestCase {

    public void testCheckRootPermission() throws Exception {
        boolean result = ShellUtil.checkRootPermission();
        assertEquals(result, false);
    }

//    public void testExec() throws Exception {
//        //TODO ggg cmd必须是有效指令
//        String[] commands = {"calc"};
//        ShellUtil.CommandResult temp = ShellUtil.exec(commands, false, true);
//        assertEquals(temp.errorMsg, "");
//    }
}
