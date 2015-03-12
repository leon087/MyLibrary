package cm.java.codec;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public final class RSACoder {

    // 非对称密钥算法
    public static final String ALG_RSA = "RSA";

    public static final int KEY_SIZE = 512;

    public static Map<RsaKeyType, byte[]> initKey(int keyLength) throws Exception {
        // 实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALG_RSA);
        // 初始化密钥生成器
        keyPairGenerator.initialize(keyLength);
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 将密钥存储在map中
        Map<RsaKeyType, byte[]> keyMap = new HashMap<RsaKeyType, byte[]>();
        keyMap.put(RsaKeyType.PUBLIC_KEY, publicKey.getEncoded());
        keyMap.put(RsaKeyType.PRIVATE_KEY, privateKey.getEncoded());
        return keyMap;
    }

    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(byte[] data, byte[] key)
            throws Exception {

        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
        // 初始化公钥
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
        // 初始化公钥
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    private static byte[] getPrivateKey(Map<RsaKeyType, byte[]> keyMap) {
        byte[] key = keyMap.get(RsaKeyType.PRIVATE_KEY);
        return key;
    }

    private static byte[] getPublicKey(Map<RsaKeyType, byte[]> keyMap)
            throws Exception {
        byte[] key = keyMap.get(RsaKeyType.PUBLIC_KEY);
        return key;
    }

    public static byte[] encrypt(RsaKeyType rsaKey, byte[] data, byte[] key) throws Exception {
        if (rsaKey.equals(RsaKeyType.PUBLIC_KEY)) {
            byte[] value = encryptByPublicKey(data, key);
            return value;
        } else {
            byte[] value = encryptByPrivateKey(data, key);
            return value;
        }
    }

    public static byte[] decrypt(RsaKeyType rsaKey, byte[] data, byte[] key) throws Exception {

        if (rsaKey.equals(RsaKeyType.PUBLIC_KEY)) {
            byte[] value = decryptByPublicKey(data, key);
            return value;
        } else {
            byte[] value = decryptByPrivateKey(data, key);
            return value;
        }
    }

    /**
     * key
     */
    public static enum RsaKeyType {
        PUBLIC_KEY(), // 公钥
        PRIVATE_KEY();// 私钥
    }
}
