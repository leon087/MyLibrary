package cm.java.codec;

import android.test.InstrumentationTestCase;

import javax.crypto.SecretKey;

import cm.java.util.Base64;
import cm.java.util.HexUtil;
import cm.java.util.Utils;

public class HashUtilTest extends InstrumentationTestCase {

    public void testGenerateHash() throws Exception {
        char[] pwd = "hello".toCharArray();
        byte[] salt = SecureUtil.SALT_DEF;
        SecretKey secretKey1 = HashUtil.generateHash(pwd, salt);
        SecretKey secretKey2 = HashUtil.generateHash(pwd, salt);

        String secretKey1String = Utils.encodeBase64(secretKey1.getEncoded());
        String secretKey2String = Utils.encodeBase64(secretKey2.getEncoded());

        boolean result = secretKey1String.equals(secretKey2String);

        assertEquals(true, result);
    }

    public void testGetShaHexAndGetSha() throws Exception {
        byte[] data = "hello".getBytes();
        String temp = HashUtil.getShaHex(data);

        final byte[] digest = HashUtil.getSha(data);
        String data1 = HexUtil.encode(digest);
        boolean result1 = data1.equalsIgnoreCase(temp);

        assertEquals(true, result1);
    }

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
