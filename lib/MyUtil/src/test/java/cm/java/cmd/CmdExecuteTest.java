package cm.java.cmd;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CmdExecuteTest {

    @Test
    public void testExec() throws Exception {
        String[] args = new String[]{
                "cat", "/proc/cpuinfo"
        };
        String temp = CmdExecute.exec(args);
        boolean result = "".equals(temp);
        assertEquals(result, false);
    }
}
