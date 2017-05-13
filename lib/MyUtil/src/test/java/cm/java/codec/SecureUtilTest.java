package cm.java.codec;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SecureUtilTest {

//    @Test
//    public void testGetIv() throws Exception {
//        byte[] data = {11};
//        IvParameterSpec ivParameterSpec = SecureUtil.getIv(data);
//        byte[] result = ivParameterSpec.getIV();
//        assertEquals(new String(data), new String(result));
//    }

    @Test
    public void testGenerateIv() throws Exception {
        byte[] result = SecureUtil.generateIv();
        assertEquals(true, result.length == 16);
    }

    @Test
    public void testGenerateSalt() throws Exception {
        byte[] result = SecureUtil.generateSalt();
        assertEquals(true, result.length == 20);
    }

    @Test
    public void testConvertSize() throws Exception {
        int result = SecureUtil.convertSize(11);
        assertEquals(88, result);
    }
}
