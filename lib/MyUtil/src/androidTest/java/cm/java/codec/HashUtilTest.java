package cm.java.codec;

import android.test.InstrumentationTestCase;

import javax.crypto.SecretKey;

import cm.java.util.HexUtil;

public class HashUtilTest extends InstrumentationTestCase {

    public void testGenerateHash() throws Exception {
        char[] password = {'s', 'a'};
        byte[] salt = {11, 22};
        SecretKey secretKey1 = HashUtil.generateHash(password, salt);
        SecretKey secretKey2 = HashUtil.generateHash(password, salt);
        boolean result = secretKey1.equals(secretKey2);
        assertEquals(false, result);
    }

    public void testGetShaHexAndGetSha() throws Exception {
        byte[] salt = {11, 22};
        String temp = HashUtil.getShaHex(salt);
        byte[] result = HexUtil.decode(temp);
        final byte[] digest = HashUtil.getSha(salt);
        boolean result1 = temp.length() > 0 && result.length > 0;

        assertEquals(true, result1);

        assertEquals(new String(result), new String(digest));
    }

    public void testGetMessageDigest() throws Exception {
        byte[] data = {11, 22};
        final byte[] temp = HashUtil.getMessageDigest(data, "123");
        boolean result = temp.length > 0;
        assertEquals(true, result);
    }

    public void testGetHmac() throws Exception {
        byte[] data = {11, 22};
        byte[] data2 = {111, 22};
        final byte[] temp = HashUtil.getHmac(data, data2);
        boolean result = temp.length > 0;
        assertEquals(true, result);
    }

}
