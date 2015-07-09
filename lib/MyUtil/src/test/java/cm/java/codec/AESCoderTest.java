package cm.java.codec;

import junit.framework.TestCase;

import javax.crypto.SecretKey;

public class AESCoderTest extends TestCase {

    public void testGenerateKey() throws Exception {
        char[] password = "aes".toCharArray();
        byte[] key1 = AESCoder.generateKey(password, null, 16).getEncoded();
        byte[] key2 = AESCoder.generateKey(password, null, 16).getEncoded();

        assertEquals(new String(key1), new String(key2));
    }

    public void testEncryptAndDecrypt() throws Exception {
        byte[] data = "aes".getBytes();

        SecretKey key = AESCoder.generateKey();
        byte[] tmp = AESCoder.encrypt(key, null, data);
        byte[] tmpData = AESCoder.decrypt(key, null, tmp);

        assertEquals(new String(data), new String(tmpData));
    }

    public void testEncryptAndDecrypt2() throws Exception {
        byte[] data = "aes".getBytes();

        byte[] key = AESCoder.generateKey().getEncoded();
        byte[] tmp = AESCoder.encrypt(key, null, data);
        byte[] tmpData = AESCoder.decrypt(key, null, tmp);

        assertEquals(new String(data), new String(tmpData));
    }
}
