package cm.java.codec;

import android.test.InstrumentationTestCase;

public class RSACoderTest extends InstrumentationTestCase {

    public void testInitKeyAndEncryptByPublicKeyAndDecryptByPrivateKey() throws Exception {
        RSACoder.Key key = RSACoder.initKey(512 / 8);
        String data = "hello";
        byte[] temp = RSACoder.encryptByPublicKey(key.publicKey, data.getBytes());
        byte[] temp1 = RSACoder.decryptByPrivateKey(key.privateKey, temp);
        assertEquals(data, new String(temp1));
    }

    public void testRSA() throws Exception {
        String data = "hello";
        RSACoder.Key keyA = RSACoder.initKey(512 / 8);
        RSACoder.Key keyB = RSACoder.initKey(512 / 8);

        // 发送方消息
        String test = "hello";

        // 发送方消息加密
        byte[] en = RSACoder.encryptByPublicKey(keyB.publicKey, test.getBytes());
        // 发送方私钥签名
        byte[] sign = RSACoder.sign(test.getBytes(), keyA.privateKey);

        // 接收方私钥解密
        byte[] de = RSACoder.decryptByPrivateKey(keyB.privateKey, en);

        // 接收方公钥解密hash
        boolean verify = RSACoder.verify(de, keyA.publicKey, sign);

        assertEquals(true, verify);
        assertEquals(test, new String(de));
    }
}
