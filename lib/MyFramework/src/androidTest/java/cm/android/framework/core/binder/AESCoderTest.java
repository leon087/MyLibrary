package cm.android.framework.core.binder;

import android.test.InstrumentationTestCase;

import javax.crypto.SecretKey;

import cm.java.codec.AESCoder;

public class AESCoderTest extends InstrumentationTestCase {

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

        byte[] encoded = AESCoder.generateKey().getEncoded();
        byte[] encrypt = AESCoder.encrypt(encoded, null, data);
        byte[] decrypt = AESCoder.decrypt(encoded, null, encrypt);

        assertEquals(new String(data), new String(decrypt));
    }
}
