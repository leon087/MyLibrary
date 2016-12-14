package cm.java.util;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;

public class IoUtilTest {

    @Test
    public void testBuildPath() throws Exception {
        File file = IoUtil.buildPath(new File("sdcard/"), "/ggg/", "/test");
        assertEquals(file.getCanonicalPath(), new File("sdcard/ggg/test").getCanonicalPath());

        File file3 = new File((File) null, "ggg/test");
        File file2 = IoUtil.buildPath(null, "ggg", "test");
        assertEquals(file2.getAbsolutePath(), file3.getAbsolutePath());
    }
}
