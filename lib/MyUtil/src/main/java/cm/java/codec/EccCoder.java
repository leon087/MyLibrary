//package cm.java.codec;
//
//import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.interfaces.ECPrivateKey;
//import java.security.interfaces.ECPublicKey;
//import java.security.spec.ECPrivateKeySpec;
//import java.security.spec.ECPublicKeySpec;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//
//import javax.crypto.Cipher;
//
//public class EccCoder {
//
//    public static final String ALG_ECC = "EC";
//
//    public static Key initKey(int keyLength) throws Exception {
//        int keySizeBit = SecureUtil.convertSize(keyLength);
//
//        // 实例化密钥生成器
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALG_ECC);
//        // 初始化密钥生成器
//        keyPairGenerator.initialize(keySizeBit);
//        // 生成密钥对
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        // 甲方公钥
//        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
//        // 甲方私钥
//        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
//
//        Key key = new Key(publicKey.getEncoded(), privateKey.getEncoded());
//        return key;
//    }
//
//    public static byte[] encryptByPrivateKey(byte[] key, byte[] data) throws Exception {
//        // 取得私钥
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
//        KeyFactory keyFactory = KeyFactory.getInstance(ALG_ECC);
//        // 生成私钥
//        ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
//        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(privateKey.getS(),
//                privateKey.getParams());
//
//        // 数据加密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.ENCRYPT_MODE, privateKey, ecPrivateKeySpec.getParams());
//        return cipher.doFinal(data);
//    }
//
//    public static byte[] encryptByPublicKey(byte[] key, byte[] data) throws Exception {
//        // 实例化密钥工厂
//        KeyFactory keyFactory = KeyFactory.getInstance(ALG_ECC);
//        // 初始化公钥
//        // 密钥材料转换
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
//        // 产生公钥
//        ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);
//        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(),
//                pubKey.getParams());
//
//        // 数据加密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());
//        return cipher.doFinal(data);
//    }
//
//    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
//        // 取得私钥
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
//        KeyFactory keyFactory = KeyFactory.getInstance(ALG_ECC);
//        // 生成私钥
//        ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
//        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(privateKey.getS(),
//                privateKey.getParams());
//
//        // 数据解密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.DECRYPT_MODE, privateKey, ecPrivateKeySpec.getParams());
//        return cipher.doFinal(data);
//    }
//
//    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
//        // 实例化密钥工厂
//        KeyFactory keyFactory = KeyFactory.getInstance(ALG_ECC);
//        // 初始化公钥
//        // 密钥材料转换
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
//        // 产生公钥
//        ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);
//        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(),
//                pubKey.getParams());
//
//        // 数据解密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.DECRYPT_MODE, pubKey, ecPublicKeySpec.getParams());
//        return cipher.doFinal(data);
//    }
//
//    public static byte[] encrypt(Type rsaKey, byte[] data, byte[] key) throws Exception {
//        if (rsaKey.equals(Type.PUBLIC)) {
//            byte[] value = encryptByPublicKey(data, key);
//            return value;
//        } else {
//            byte[] value = encryptByPrivateKey(data, key);
//            return value;
//        }
//    }
//
//    public static byte[] decrypt(Type rsaKey, byte[] data, byte[] key) throws Exception {
//        if (rsaKey.equals(Type.PUBLIC)) {
//            byte[] value = decryptByPublicKey(data, key);
//            return value;
//        } else {
//            byte[] value = decryptByPrivateKey(data, key);
//            return value;
//        }
//    }
//
//    /**
//     * key
//     */
//    public static enum Type {
//        PUBLIC, // 公钥
//        PRIVATE;// 私钥
//    }
//
//    public static class Key {
//
//        public final byte[] publicKey;
//
//        public final byte[] privateKey;
//
//        Key(byte[] publicKey, byte[] privateKey) {
//            this.publicKey = publicKey;
//            this.privateKey = privateKey;
//        }
//    }
//}
