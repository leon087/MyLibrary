package cm.java.codec;

import android.test.InstrumentationTestCase;

public class RSACoderTest extends InstrumentationTestCase {

    public void testInitKeyAndEncryptByPublicKeyAndDecryptByPrivateKey() throws Exception {
        RSACoder.Key key = RSACoder.initKey(512 / 8);
        String data = "hello";
        byte[] temp = RSACoder.encryptByPublicKey(data.getBytes(), key.publicKey);
        byte[] temp1 = RSACoder.decryptByPrivateKey(temp, key.privateKey);
        assertEquals(data, new String(temp1));
    }

    public void testInitKeyAndEncryptByPrivateKeyAndDecryptByPublicKey() throws Exception {
        RSACoder.Key key = RSACoder.initKey(512 / 8);
        String data = "hello";
        byte[] temp = RSACoder.encryptByPrivateKey(data.getBytes(), key.privateKey);
        byte[] temp1 = RSACoder.decryptByPublicKey(temp, key.publicKey);
        assertEquals(data, new String(temp1));
    }
}
