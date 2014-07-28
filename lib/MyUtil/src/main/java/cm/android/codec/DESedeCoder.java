package cm.android.codec;

import org.apache.commons.codec.binary.ApacheBase64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;

/**
 * DESede对称加密算法演示
 * <p/>
 * <p/>
 * 对称加密算法就是能将数据加解密。加密的时候用密钥对数据进行加密，解密的时候使用同样的密钥对数据进行解密。<br>
 * <br>
 * DES是美国国家标准研究所提出的算法。因为加解密的数据安全性和密钥长度成正比
 * 。des的56位的密钥已经形成安全隐患，在1998年之后就很少被采用。但是一些老旧的系统还在使用
 * 。因为这个des算法并没有被美国标准委员会公布全部算法，大家一致怀疑被留了后门。所以慢慢就被淘汰掉了。<br>
 * <br>
 * 后来针对des算法进行了改进，有了三重des算法（DESede
 * ）。针对des算法的密钥长度较短以及迭代次数偏少问题做了相应改进，提高了安全强度。不过desede算法处理速度较慢
 * ，密钥计算时间较长，加密效率不高问题使得对称加密算法的发展不容乐观。<br>
 * <br>
 * {@link http://blog.csdn.net/kongqz/article/category/800296}
 */
public class DESedeCoder {
    /**
     * 密钥算法
     */
    public static final String KEY_ALGORITHM = "DESede";

    /**
     * 加密/解密算法/工作模式/填充方式
     */
    public static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";

    /**
     * 生成密钥
     *
     * @return byte[] 二进制密钥
     */
    public static byte[] initkey() throws Exception {

        // 实例化密钥生成器
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器
        kg.init(168);
        // 生成密钥
        SecretKey secretKey = kg.generateKey();
        // 获取二进制密钥编码形式
        return secretKey.getEncoded();
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key 密钥
     */
    public static Key toKey(byte[] key) throws Exception {
        // 实例化Des密钥
        DESedeKeySpec dks = new DESedeKeySpec(key);
        // 实例化密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance(KEY_ALGORITHM);
        // 生成密钥
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密后的数据
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 还原密钥
        Key k = toKey(key);
        // 实例化
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密后的数据
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 欢迎密钥
        Key k = toKey(key);
        // 实例化
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 进行加解密的测试
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String str = "DESede";
        System.out.println("原文：" + str);
        // 初始化密钥
        byte[] key = DESedeCoder.initkey();
        System.out.println("密钥：" + ApacheBase64.encodeBase64String(key));
        // 加密数据
        byte[] data = DESedeCoder.encrypt(str.getBytes(), key);
        System.out.println("加密后：" + ApacheBase64.encodeBase64String(data));
        // 解密数据
        data = DESedeCoder.decrypt(data, key);
        System.out.println("解密后：" + new String(data));
    }
}
