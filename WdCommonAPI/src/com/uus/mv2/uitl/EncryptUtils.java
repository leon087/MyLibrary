package com.uus.mv2.uitl;

import android.content.Context;

public class EncryptUtils {

	static {
		System.loadLibrary("ENCRYPT");
	}

	/**
	 * 初始化lib，建议放入application的create中
	 * 
	 * @param context
	 */
	public static native void initLib(Context context);

	/**
	 * http post加密使用
	 * 
	 * @param str
	 * @return
	 */
	public static native byte[] encrypt(String str);

	/**
	 * token 加密使用
	 * 
	 * @param str
	 * @return
	 */
	public static native byte[] encrypt2(String str);

	/**
	 * http post解密使用
	 * 
	 * @param str
	 * @return
	 */
	public static native byte[] decrypt(byte[] str);

	/**
	 * token解密使用
	 * 
	 * @param str
	 * @return
	 */
	public static native byte[] decrypt2(String key, String data);
	
	/**
	 * token获取指纹
	 * 
	 * @param str
	 * @return
	 */
	public static native String getTKInfo(String ua,String tk);

	
	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
}
