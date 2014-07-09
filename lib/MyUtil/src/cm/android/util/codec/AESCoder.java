package cm.android.util.codec;

import cm.android.util.util.Base64Util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * AES对称加密算法，java6实现，bouncycastle也支持AES对称加密算法
 * 我们可以以AES算法实现为参考，完成RC2，RC4和Blowfish算法的实现
 * 
 * http://blog.csdn.net/kongqz/article/details/6287257
 * */
public class AESCoder {
	/**
	 * 密钥算法 java6支持56位密钥，bouncycastle支持64位
	 * */
	public static final String KEY_ALGORITHM = "AES";

	/**
	 * 加密/解密算法/工作模式/填充方式
	 * 
	 * JAVA6 支持PKCS5PADDING填充方式 Bouncy castle支持PKCS7Padding填充方式
	 * */
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * 
	 * 生成密钥，java6只支持56位密钥，bouncycastle支持64位密钥
	 * 
	 * @return byte[] 二进制密钥
	 * */
	public static byte[] initKey() throws Exception {

		// 实例化密钥生成器
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		// 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
		kg.init(128);
		// 生成密钥
		SecretKey secretKey = kg.generateKey();
		// 获取二进制密钥编码形式
		return secretKey.getEncoded();
	}

	public static String initBase64Key() throws Exception {
		byte[] key = AESCoder.initKey();
		return Base64Util.encodeBase64String(key);
	}

	/**
	 * 转换密钥
	 * 
	 * @param key
	 *            二进制密钥
	 * @return Key 密钥
	 * */
	public static Key toKey(byte[] key) throws Exception {
		// 实例化DES密钥
		// 生成密钥
		SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
		return secretKey;
	}

	/**
	 * 加密数据
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密后的数据
	 * */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 还原密钥
		Key k = toKey(key);
		/**
		 * 实例化 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现
		 * Cipher.getInstance(CIPHER_ALGORITHM,"BC")
		 */
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		// 执行操作
		return cipher.doFinal(data);
	}

	/**
	 * 解密数据
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密后的数据
	 * */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 欢迎密钥
		Key k = toKey(key);
		/**
		 * 实例化 使用 PKCS7PADDING 填充方式，按如下方式实现,就是调用bouncycastle组件实现
		 * Cipher.getInstance(CIPHER_ALGORITHM,"BC")
		 */
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		// 执行操作
		return cipher.doFinal(data);
	}

	/**
	 * 将数组经AES加密后转成Base64编码
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBase64(byte[] data, String base64Key)
			throws Exception {
		byte[] key = Base64Util.decodeBase64(base64Key);
		byte[] value = encrypt(data, key);
		return Base64Util.encodeBase64String(value);
	}

	/**
	 * 将Base64编码格式的数组解密
	 * 
	 * @param base64Data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBase64(String base64Data, String base64Key)
			throws Exception {
		byte[] key = Base64Util.decodeBase64(base64Key);
		byte[] data = Base64Util.decodeBase64(base64Data);
		byte[] value = AESCoder.decrypt(data, key);
		return value;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String str = "AES";
		System.out.println("原文：" + str);
		// 初始化密钥
		String base64Key = AESCoder.initBase64Key();
		System.out.println("密钥：" + base64Key);
		// 加密数据
		String base64Data = AESCoder.encryptBase64(str.getBytes(), base64Key);
		System.out.println("加密后：" + base64Data);
		// 解密数据
		String data = new String(AESCoder.decryptBase64(base64Data, base64Key));
		System.out.println("解密后：" + data);

		// String str = "AES";
		// System.out.println("原文：" + str);
		// // 初始化密钥
		// byte[] key = AESCoder.initkey();
		// System.out.println("密钥：" + Base64.encodeBase64String(key));
		// // 加密数据
		// byte[] data = AESCoder.encrypt(str.getBytes(), key);
		// System.out.println("加密后：" + Base64.encodeBase64String(data));
		// // 解密数据
		// data = AESCoder.decrypt(data, key);
		// System.out.println("解密后：" + new String(data));
	}
}
