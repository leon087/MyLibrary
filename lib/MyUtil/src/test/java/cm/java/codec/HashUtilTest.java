package cm.java.codec;

import org.junit.Test;

import javax.crypto.SecretKey;

import cm.java.util.Utils;

import static junit.framework.Assert.assertEquals;

public class HashUtilTest {

    @Test
    public void testGenerateHash() throws Exception {
        char[] pwd = "hello".toCharArray();
        byte[] salt = SecureUtil.getSaltDef();
        SecretKey secretKey1 = HashUtil.generateHash(pwd, salt);
        SecretKey secretKey2 = HashUtil.generateHash(pwd, salt);

        String secretKey1String = Utils.encodeBase64(secretKey1.getEncoded());
        String secretKey2String = Utils.encodeBase64(secretKey2.getEncoded());

        boolean result = secretKey1String.equals(secretKey2String);

        assertEquals(true, result);
    }

//    @Test
//    public void testGetShaHexAndGetSha() throws Exception {
//        byte[] data = "hello".getBytes();
//        String temp = HashUtil.getShaHex(data);
//
//        final byte[] digest = HashUtil.getSha(data);
//        String data1 = HexUtil.encode(digest);
//        boolean result1 = data1.equalsIgnoreCase(temp);
//
//        assertEquals(true, result1);
//    }

    @Test
    public void testGetHmac() throws Exception {
        byte[] data = "hello".getBytes();
        byte[] data2 = "world".getBytes();
        final byte[] temp = HashUtil.getHmac(data, data2);
        final byte[] temp2 = HashUtil.getHmac(data, data2);
        boolean result = new String(temp).equals("");
        assertEquals(result, false);
        assertEquals(new String(temp), new String(temp2));
    }
}
