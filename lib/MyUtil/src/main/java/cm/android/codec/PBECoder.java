package cm.android.codec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 对称加密算法：基于口令加密-PBE算法实现
 */
public class PBECoder {
    // change to SC if using Spongycastle crypto libraries
    public static final String PROVIDER = "BC";

    private static final String BACKUP_PBE_KEY_ALG = "PBEWithMD5AndDES";
    private static final int ITERATIONS = 2000;

    public static Key toKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 密钥彩礼转换
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, ITERATIONS);
        SecretKeyFactory keyFactory = null;
        // 实例化
        keyFactory = SecretKeyFactory.getInstance(BACKUP_PBE_KEY_ALG);
        // 生成密钥
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        return secretKey;
    }

    /**
     * 加密
     *
     * @param data     待加密数据
     * @param password 密码
     * @param salt     盐
     * @return byte[] 加密数据
     */
    public static byte[] encrypt(byte[] data, char[] password, byte[] salt)
            throws Exception {
        // 转换密钥
        Key key = toKey(password, salt);
        // 实例化PBE参数材料
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
        // 实例化
        Cipher cipher = null;
        cipher = Cipher.getInstance(BACKUP_PBE_KEY_ALG, PROVIDER);

        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data     待解密数据
     * @param password 密码
     * @param salt     盐
     * @return byte[] 解密数据
     */
    public static byte[] decrypt(byte[] data, char[] password, byte[] salt)
            throws Exception {
        // 转换密钥
        Key key = toKey(password, salt);
        // 实例化PBE参数材料
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
        // 实例化
        Cipher cipher = null;
        cipher = Cipher.getInstance(BACKUP_PBE_KEY_ALG, PROVIDER);
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        // 执行操作
        return cipher.doFinal(data);
    }
}
