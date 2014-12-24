package cm.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * MD5工具
 */
public class Md5Util {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private Md5Util() {
    }

    public static String md5Code(String str) {
        try {
            byte[] res = str.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5".toUpperCase());
            byte[] result = md.digest(res);
            for (int i = 0; i < result.length; i++) {
                md.update(result[i]);
            }
            byte[] hash = md.digest();
            StringBuffer d = new StringBuffer("");
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i] & 0xFF;
                if (v < 16) {
                    d.append("0");
                }
                d.append(Integer.toString(v, 16).toUpperCase() + "");
            }
            return d.toString();
        } catch (Exception e) {
            return "";
        }

    }

    // 通用加密
    public static String md5CodeCommon(String plainStr) {
        byte[] source = plainStr.getBytes();
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();

            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getFileMD5(File file) {
        byte[] digest = getMd5Digest(file);
        if (digest == null) {
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest);
        return bigInt.toString(16);
    }

    public static byte[] getMd5Digest(File file) {
        if (!file.isFile()) {
            return null;
        }

        InputStream inputStream = null;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest digest;
        try {
            inputStream = new FileInputStream(file);
            digest = MessageDigest.getInstance("MD5");
            while ((numRead = inputStream.read(buffer)) > 0) {
                digest.update(buffer, 0, numRead);
            }
            return digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IoUtil.closeQuietly(inputStream);
        }
    }

    public static String md5sum(File file) {
        byte[] digest = getMd5Digest(file);
        if (digest == null) {
            return null;
        }

        return ByteUtil.toHexString(digest);
    }
}
