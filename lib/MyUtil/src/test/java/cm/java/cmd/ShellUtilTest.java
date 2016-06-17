package cm.java.cmd;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ShellUtilTest {

    @Test
    public void testCheckRootPermission() throws Exception {
        boolean result = ShellUtil.checkRootPermission();
        assertEquals(result, true);
    }

//    public void testExec() throws Exception {
//        //TODO ggg cmd必须是有效指令
//        String[] commands = {"calc"};
//        ShellUtil.CommandResult temp = ShellUtil.exec(commands, false, true);
//        assertEquals(temp.errorMsg, "");
//    }
}
