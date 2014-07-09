package cm.android.util.util;

import org.apache.commons.codec.binary.ApacheBase64;

public class Base64Util {
	/**
	 * 将byte[]编码成Base64格式
	 * 
	 * @return
	 */
	public static String encodeBase64String(byte[] data) {
		return ApacheBase64.encodeBase64String(data);
	}

	/**
	 * 将Base64编码格式的String解码成byte[]
	 * 
	 * @param base64Data
	 * @return
	 */
	public static byte[] decodeBase64(String base64Data) {
		return ApacheBase64.decodeBase64(base64Data);
	}

	/**
	 * 将byte[]编码成Base64格式，相对于encodeBase64String，将浏览器中不允许出现的+=号给替换成-.这些符号，用以生成url
	 */
	public static String encodeBase64URLSafe(byte[] binaryData) {
		return new String(ApacheBase64.encodeBase64URLSafe(binaryData));
	}

	public static void main(String[] args) {
		String s = "你好啊呵呵呵";
		String value = encodeBase64URLSafe(s.getBytes());
		byte[] sValue = Base64Util.decodeBase64(value);
		System.out.println(new String(sValue));
	}

}
