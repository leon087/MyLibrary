package cm.java.codec;

import android.test.InstrumentationTestCase;

import java.util.Map;

public class RSACoderTest extends InstrumentationTestCase {

    public void testInitKeyAndEncryptByPublicKeyAndDecryptByPrivateKey() throws Exception {
        Map<RSACoder.RsaKeyType, byte[]> keyMap = RSACoder.initKey(11);
        byte[] data = {1, 2, 3};
        byte[] temp = RSACoder.encryptByPublicKey(data, keyMap.get(RSACoder.RsaKeyType.PUBLIC_KEY));
        byte[] temp1 = RSACoder
                .decryptByPrivateKey(temp, keyMap.get(RSACoder.RsaKeyType.PRIVATE_KEY));
        assertEquals(new String(data), new String(temp1));
    }

    public void testInitKeyAndEncryptByPrivateKeyAndDecryptByPublicKey() throws Exception {
        Map<RSACoder.RsaKeyType, byte[]> keyMap = RSACoder.initKey(11);
        byte[] data = {1, 2, 3};
        byte[] temp = RSACoder
                .encryptByPrivateKey(data, keyMap.get(RSACoder.RsaKeyType.PRIVATE_KEY));
        byte[] temp1 = RSACoder
                .decryptByPublicKey(temp, keyMap.get(RSACoder.RsaKeyType.PUBLIC_KEY));
        assertEquals(new String(data), new String(temp1));
    }
}
