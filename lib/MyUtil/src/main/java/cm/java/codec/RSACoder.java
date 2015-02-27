package cm.java.codec;//package cm.android.codec;
//
//import org.apache.commons.codec.binary.Base64;
//
//import cm.android.util.Base64;
//
//import javax.crypto.Cipher;
//import java.security.*;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 非对称加密算法RSA算法组件 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
// * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
// * <p/>
// * {@link http://blog.csdn.net/kongqz/article/details/6302980}
// */
//public class RSACoder {
//    // 非对称密钥算法
//    public static final String KEY_ALGORITHM = "RSA";
//
//    /**
//     * 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到65536位之间
//     */
//    private static final int KEY_SIZE = 512;
//
//    /**
//     * 初始化密钥对
//     *
//     * @return Map 甲方密钥的Map
//     */
//    public static Map<RsaKeyType, Object> initKey() throws Exception {
//        // 实例化密钥生成器
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator
//                .getInstance(KEY_ALGORITHM);
//        // 初始化密钥生成器
//        keyPairGenerator.initialize(KEY_SIZE);
//        // 生成密钥对
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//        // 甲方公钥
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        // 甲方私钥
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        // 将密钥存储在map中
//        Map<RsaKeyType, Object> keyMap = new HashMap<RsaKeyType, Object>();
//        keyMap.put(RsaKeyType.PUBLIC_KEY, publicKey);
//        keyMap.put(RsaKeyType.PRIVATE_KEY, privateKey);
//        return keyMap;
//    }
//
//    /**
//     * 私钥加密
//     *
//     * @param data待加密数据
//     * @param key       密钥
//     * @return byte[] 加密数据
//     */
//    public static byte[] encryptByPrivateKey(byte[] data, byte[] key)
//            throws Exception {
//
//        // 取得私钥
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        // 生成私钥
//        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
//        // 数据加密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//        return cipher.doFinal(data);
//    }
//
//    /**
//     * 公钥加密
//     *
//     * @param data待加密数据
//     * @param key       密钥
//     * @return byte[] 加密数据
//     */
//    public static byte[] encryptByPublicKey(byte[] data, byte[] key)
//            throws Exception {
//
//        // 实例化密钥工厂
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        // 初始化公钥
//        // 密钥材料转换
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
//        // 产生公钥
//        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
//
//        // 数据加密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
//        return cipher.doFinal(data);
//    }
//
//    /**
//     * 私钥解密
//     *
//     * @param data 待解密数据
//     * @param key  密钥
//     * @return byte[] 解密数据
//     */
//    public static byte[] decryptByPrivateKey(byte[] data, byte[] key)
//            throws Exception {
//        // 取得私钥
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        // 生成私钥
//        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
//        // 数据解密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        return cipher.doFinal(data);
//    }
//
//    /**
//     * 公钥解密
//     *
//     * @param data 待解密数据
//     * @param key  密钥
//     * @return byte[] 解密数据
//     */
//    public static byte[] decryptByPublicKey(byte[] data, byte[] key)
//            throws Exception {
//
//        // 实例化密钥工厂
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        // 初始化公钥
//        // 密钥材料转换
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
//        // 产生公钥
//        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
//        // 数据解密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//        cipher.init(Cipher.DECRYPT_MODE, pubKey);
//        return cipher.doFinal(data);
//    }
//
//    /**
//     * 取得私钥
//     *
//     * @param keyMap 密钥map
//     * @return byte[] 私钥
//     */
//    public static byte[] getPrivateKey(Map<RsaKeyType, Object> keyMap) {
//        Key key = (Key) keyMap.get(RsaKeyType.PRIVATE_KEY);
//        return key.getEncoded();
//    }
//
//    /**
//     * 取得公钥
//     *
//     * @param keyMap 密钥map
//     * @return byte[] 公钥
//     */
//    public static byte[] getPublicKey(Map<RsaKeyType, Object> keyMap)
//            throws Exception {
//        Key key = (Key) keyMap.get(RsaKeyType.PUBLIC_KEY);
//        return key.getEncoded();
//    }
//
//    /**
//     * 初始化经过Base64编码的key
//     *
//     * @return
//     * @throws Exception
//     */
//    public static Map<RsaKeyType, String> initBase64Key() throws Exception {
//        Map<RsaKeyType, Object> keyMap = RSACoder.initKey();
//        // 公钥
//        byte[] publicKey = RSACoder.getPublicKey(keyMap);
//
//        // 私钥
//        byte[] privateKey = RSACoder.getPrivateKey(keyMap);
//
//        String base64PublicKey = Base64.encodeBase64String(publicKey);
//        String base64PrivateKey = Base64.encodeBase64String(privateKey);
//        Map<RsaKeyType, String> base64KeyMap = new HashMap<RSACoder.RsaKeyType, String>();
//        base64KeyMap.put(RsaKeyType.PUBLIC_KEY, base64PublicKey);
//        base64KeyMap.put(RsaKeyType.PRIVATE_KEY, base64PrivateKey);
//        return base64KeyMap;
//    }
//
//    /**
//     * 根据base64编码的key加密byte[]并将加密后的数据编码成Base64格式
//     *
//     * @param rsaKey
//     * @param data      原始数据
//     * @param base64Key base64编码过的key
//     * @return 经base64编码过的加密数据
//     * @throws Exception
//     */
//    public static String encryptBase64(RsaKeyType rsaKey, byte[] data,
//                                       String base64Key) throws Exception {
//        byte[] key = Base64.decodeBase64(base64Key);
//
//        if (rsaKey.equals(RsaKeyType.PUBLIC_KEY)) {
//            byte[] value = encryptByPublicKey(data, key);
//            return Base64.encodeBase64String(value);
//        } else {
//            byte[] value = encryptByPrivateKey(data, key);
//            return Base64.encodeBase64String(value);
//        }
//    }
//
//    /**
//     * 根据base64编码的key解密经Base64编码过的加密数据
//     *
//     * @param rsaKey
//     * @param base64Data base64编码过的加密数据
//     * @param base64Key  base64编码过的key
//     * @return
//     * @throws Exception
//     */
//    public static byte[] decryptBase64(RsaKeyType rsaKey, String base64Data,
//                                       String base64Key) throws Exception {
//        byte[] key = org.apache.commons.codec.binary.Base64.decodeBase64(base64Key);
//        byte[] data = org.apache.commons.codec.binary.Base64.decodeBase64(base64Data);
//
//        if (rsaKey.equals(RsaKeyType.PUBLIC_KEY)) {
//            byte[] value = decryptByPublicKey(data, key);
//            return value;
//        } else {
//            byte[] value = decryptByPrivateKey(data, key);
//            return value;
//        }
//    }
//
//    /**
//     * @param args
//     * @throws Exception
//     */
//    public static void main(String[] args) throws Exception {
//        // 初始化密钥
//        // 生成密钥对
//        Map<RsaKeyType, String> base64keyMap = RSACoder.initBase64Key();
//        String publicKey = base64keyMap.get(RsaKeyType.PUBLIC_KEY);
//        String privateKey = base64keyMap.get(RsaKeyType.PRIVATE_KEY);
//
//        System.out.println("公钥：" + publicKey);
//        System.out.println("私钥：" + privateKey);
//
//        System.out
//                .println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
//        String str = "RSA密码交换算法";
//        System.out.println("\n===========甲方向乙方发送加密数据==============");
//        System.out.println("原文:" + str);
//        // 甲方进行数据的加密
//        String base64Data = RSACoder.encryptBase64(RsaKeyType.PRIVATE_KEY,
//                str.getBytes(), privateKey);
//        System.out.println("加密后的数据：" + base64Data);
//        System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
//        // 乙方进行数据的解密
//        byte[] decode1 = RSACoder.decryptBase64(RsaKeyType.PUBLIC_KEY,
//                base64Data, publicKey);
//        System.out.println("乙方解密后的数据：" + new String(decode1) + "\n\n");
//
//        // //
//        System.out.println("===========反向进行操作，乙方向甲方发送数据==============\n\n");
//        str = "乙方向甲方发送数据RSA算法";
//        System.out.println("原文:" + str);
//
//        // 乙方使用公钥对数据进行加密
//        base64Data = RSACoder.encryptBase64(RsaKeyType.PUBLIC_KEY,
//                str.getBytes(), publicKey);
//        System.out.println("===========乙方使用公钥对数据进行加密==============");
//        System.out.println("加密后的数据：" + base64Data);
//
//        System.out.println("=============乙方将数据传送给甲方======================");
//        System.out.println("===========甲方使用私钥对数据进行解密==============");
//        // 甲方使用私钥对数据进行解密
//        byte[] decode2 = RSACoder.decryptBase64(RsaKeyType.PRIVATE_KEY,
//                base64Data, privateKey);
//
//        System.out.println("甲方解密后的数据：" + new String(decode2));
//
//        // // 初始化密钥
//        // // 生成密钥对
//        // Map<RsaKey, Object> keyMap = RSACoder.initKey();
//        // // 公钥
//        // byte[] publicKey = RSACoder.getPublicKey(keyMap);
//        //
//        // // 私钥
//        // byte[] privateKey = RSACoder.getPrivateKey(keyMap);
//        //
//        // System.out.println("公钥：" + Base64.encodeBase64String(publicKey));
//        // System.out.println("私钥：" + Base64.encodeBase64String(privateKey));
//        //
//        // System.out
//        // .println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
//        // String str = "RSA密码交换算法";
//        // System.out.println("/n===========甲方向乙方发送加密数据==============");
//        // System.out.println("原文:" + str);
//        // // 甲方进行数据的加密
//        // byte[] code1 = RSACoder.encryptByPrivateKey(str.getBytes(),
//        // privateKey);
//        // System.out.println("加密后的数据：" + Base64.encodeBase64String(code1));
//        // System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
//        // // 乙方进行数据的解密
//        // byte[] decode1 = RSACoder.decryptByPublicKey(code1, publicKey);
//        // System.out.println("乙方解密后的数据：" + new String(decode1) + "/n/n");
//        //
//        // System.out.println("===========反向进行操作，乙方向甲方发送数据==============/n/n");
//        //
//        // str = "乙方向甲方发送数据RSA算法";
//        //
//        // System.out.println("原文:" + str);
//        //
//        // // 乙方使用公钥对数据进行加密
//        // byte[] code2 = RSACoder.encryptByPublicKey(str.getBytes(),
//        // publicKey);
//        // System.out.println("===========乙方使用公钥对数据进行加密==============");
//        // System.out.println("加密后的数据：" + Base64.encodeBase64String(code2));
//        //
//        // System.out.println("=============乙方将数据传送给甲方======================");
//        // System.out.println("===========甲方使用私钥对数据进行解密==============");
//        //
//        // // 甲方使用私钥对数据进行解密
//        // byte[] decode2 = RSACoder.decryptByPrivateKey(code2, privateKey);
//        //
//        // System.out.println("甲方解密后的数据：" + new String(decode2));
//    }
//
//    /**
//     * key
//     */
//    public static enum RsaKeyType {
//        PUBLIC_KEY("RSAPublicKey"), // 公钥
//        PRIVATE_KEY("RSAPrivateKey");// 私钥
//
//        private String type;
//
//        private RsaKeyType(String type) {
//            this.type = type;
//        }
//    }
//}
