package cm.java.codec;

import org.junit.Test;

import javax.crypto.SecretKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AESCoderTest {

    @Test
    public void testGenerateKey() throws Exception {
        char[] password = "aes".toCharArray();
        byte[] key1 = AESCoder.generateKey(password, SecureUtil.getSaltDef(), 16).getEncoded();
        byte[] key2 = AESCoder.generateKey(password, SecureUtil.getSaltDef(), 16).getEncoded();

        assertEquals(new String(key1), new String(key2));
        assertTrue(true);
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        byte[] data = "aes".getBytes();

        SecretKey key = AESCoder.generateKey();
        byte[] tmp = AESCoder.encrypt(key, SecureUtil.getIvDef(), data);
        byte[] tmpData = AESCoder.decrypt(key, SecureUtil.getIvDef(), tmp);

        assertEquals(new String(data), new String(tmpData));
    }

    @Test
    public void testEncryptAndDecrypt2() throws Exception {
        byte[] data = "aes".getBytes();

        byte[] key = AESCoder.generateKey().getEncoded();
        byte[] tmp = AESCoder.encrypt(key, SecureUtil.getIvDef(), data);
        byte[] tmpData = AESCoder.decrypt(key, SecureUtil.getIvDef(), tmp);

        assertEquals(new String(data), new String(tmpData));
    }
}
