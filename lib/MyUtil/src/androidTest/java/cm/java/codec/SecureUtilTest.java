package cm.java.codec;

import android.test.InstrumentationTestCase;

import java.security.SecureRandom;

import javax.crypto.spec.IvParameterSpec;

public class SecureUtilTest extends InstrumentationTestCase {

    public void testGetIv() throws Exception {
        byte[] data = {11};
        IvParameterSpec ivParameterSpec = SecureUtil.getIv(data);
        byte[] result = ivParameterSpec.getIV();
        assertEquals(new String(data), new String(result));
    }

    public void testGenerateIv() throws Exception {
        byte[] result = SecureUtil.generateIv();
        assertEquals(true, result.length == 16);
    }

    public void testGenerateSalt() throws Exception {
        byte[] result = SecureUtil.generateSalt();
        assertEquals(true, result.length == 20);
    }

    public void testConvertSize() throws Exception {
        int result = SecureUtil.convertSize(11);
        assertEquals(88, result);
    }
}
