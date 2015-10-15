package cm.java.codec;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * openssl生成key时需转换成PKCS#8
 */
public final class RSACoder {

    // 非对称密钥算法
    public static final String ALG_RSA = "RSA";

    /**
     * 不能一次性对超过117个字节的数据加密
     */
    public static final String ALG_RSA_ECB_PKCS1 = "RSA/ECB/PKCS1Padding";

    /**
     * BouncyCastleProvider
     */
    public static final String PROVIDER_BC = "BC";

    public static final String ALG_SIGNATURE = "SHA256withRSA";

    static {
        // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private RSACoder() {
    }

    public static Key initKey(int keyLength) throws Exception {
        int keySizeBit = SecureUtil.convertSize(keyLength);

        // 实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALG_RSA);
        // 初始化密钥生成器
        keyPairGenerator.initialize(keySizeBit);
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Key key = new Key(publicKey.getEncoded(), privateKey.getEncoded());
        return key;
    }

    public static byte[] encryptByPublicKey(byte[] key, byte[] data) throws Exception {
        // 产生公钥
        PublicKey pubKey = generatePublic(key);

        // 数据加密
        Cipher cipher = Cipher.getInstance(ALG_RSA_ECB_PKCS1);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(byte[] key, byte[] data) throws Exception {
        // 生成私钥
        PrivateKey privateKey = generatePrivate(key);

        // 数据解密
        Cipher cipher = Cipher.getInstance(ALG_RSA_ECB_PKCS1);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    //    public static byte[] encryptByPrivateKey(byte[] key, byte[] data) throws Exception {
//        // 生成私钥
//        PrivateKey privateKey = generatePrivate(key);
//
//        // 数据加密
//        KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//        return cipher.doFinal(data);
//    }

//    public static byte[] decryptByPublicKey(byte[] key, byte[] data) throws Exception {
//        // 产生公钥
//        PublicKey pubKey = generatePublic(key);
//
//        // 数据解密
//        KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.DECRYPT_MODE, pubKey);
//        return cipher.doFinal(data);
//    }

    private static PublicKey generatePublic(byte[] publicByte)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicByte);
        // 产生公钥
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

        return publicKey;
    }

    private static PrivateKey generatePrivate(byte[] privateByte)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateByte);
        KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        return privateKey;
    }

    /**
     * 用私钥对信息生成数字签名
     */
    public static byte[] sign(byte[] data, byte[] privateByte) throws Exception {
        PrivateKey privateKey = generatePrivate(privateByte);

        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(ALG_SIGNATURE);
        signature.initSign(privateKey);
        signature.update(data);

        return signature.sign();
    }

    /**
     * 校验数字签名
     */
    public static boolean verify(byte[] data, byte[] publicByte, byte[] sign) throws Exception {
        //取公钥匙对象
        PublicKey publicKey = generatePublic(publicByte);

        Signature signature = Signature.getInstance(ALG_SIGNATURE);
        signature.initVerify(publicKey);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(sign);
    }

    public static class Key {

        public final byte[] publicKey;

        public final byte[] privateKey;

        Key(byte[] publicKey, byte[] privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

//    public static void main(String[] args) throws Exception {
//        Key keyA = RSACoder.initKey(512 / 8);
//        Key keyB = RSACoder.initKey(512 / 8);
//
//        // 发送方消息
//        String test = "hello";
//
//        // 发送方消息加密
//        byte[] en = RSACoder.encryptByPublicKey(keyB.publicKey, test.getBytes());
//        // 发送方私钥签名
//        byte[] sign = RSACoder.sign(test.getBytes(), keyA.privateKey);
//
//        System.out.println("test = " + test);
//        System.out.println("sign = " + Utils.byte2hexString(sign));
//
//        System.out.println("en = " + Utils.byte2hexString(en));
//
//        // 接收方私钥解密
//        byte[] de = RSACoder.decryptByPrivateKey(keyB.privateKey, en);
//
//        // 接收方公钥解密hash
//        boolean verify = RSACoder.verify(de, keyA.publicKey, sign);
//
//        System.out.println("de = " + new String(de));
//        System.out.println("verify = " + verify);
//    }
}
