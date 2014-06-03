package wd.android.common.download;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 下载功能相关的工具类。
 * 
 * @author
 * 
 */
public class DownloadUtils {

	// /**
	// * 获取相应类型的文件路径
	// *
	// * @param name
	// * @param type
	// * @return String
	// */
	// public static String initImgPath(String name, int type) {
	// String imgPath = "";
	// if (name == null || "".equals(name.trim())) {
	// return imgPath;
	// }
	// String showName = name;
	// if (showName != null && !"".equals(showName)) {
	// File fileDir = new File(DownloadConstants.SDCARD_PATH
	// + DownloadConstants.DOWNLOAD_FOlDER);
	// if (fileDir != null && fileDir.exists()) {
	// String[] fileList = fileDir.list();
	// if (fileList != null && fileList.length > 0) {
	// for (int i = 0; i < fileList.length; i++) {
	// if (type == DownloadConstants.FILE_TYPE_IMG) {
	// if ((showName + DownloadConstants.TAG_IMG_TYPE_JPG)
	// .equalsIgnoreCase(fileList[i])) {
	// imgPath = DownloadConstants.SDCARD_PATH
	// + DownloadConstants.DOWNLOAD_FOlDER
	// + "/"
	// + (showName + DownloadConstants.TAG_IMG_TYPE_JPG);
	// return imgPath;
	// }
	// if ((showName + DownloadConstants.TAG_IMG_TYPE_PNG)
	// .equalsIgnoreCase(fileList[i])) {
	// imgPath = DownloadConstants.SDCARD_PATH
	// + DownloadConstants.DOWNLOAD_FOlDER
	// + "/"
	// + (showName + DownloadConstants.TAG_IMG_TYPE_PNG);
	// return imgPath;
	// }
	// if ((showName + DownloadConstants.TAG_IMG_TYPE_JPEG)
	// .equalsIgnoreCase(fileList[i])) {
	// imgPath = DownloadConstants.SDCARD_PATH
	// + DownloadConstants.DOWNLOAD_FOlDER
	// + "/"
	// + (showName + DownloadConstants.TAG_IMG_TYPE_JPEG);
	// return imgPath;
	// }
	// } else if (type == DownloadConstants.FILE_TYPE_TXT) {
	// if ((showName + DownloadConstants.TAG_IMG_TYPE_DESC)
	// .equalsIgnoreCase(fileList[i])) {
	// imgPath = DownloadConstants.SDCARD_PATH
	// + DownloadConstants.DOWNLOAD_FOlDER
	// + "/"
	// + (showName + DownloadConstants.TAG_IMG_TYPE_DESC);
	// return imgPath;
	// }
	// }
	// }
	// }
	// }
	// }
	// return imgPath;
	// }

	/**
	 * 根据建立好的http链接获取文件大小
	 * 
	 * @param http
	 * @return 文件大小
	 */
	public static long getFileTotalSize(HttpURLConnection http) {
		long totalLength = 0;
		Map<String, String> header = getHttpResponseHeader(http);
		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey() != null ? entry.getKey() : "";
			if ("fileSize".equalsIgnoreCase(key)) {
				String length = entry.getValue();
				if (length != null && !"".equals(length.trim()))
					totalLength = Long.valueOf(length);
			}
		}
		return totalLength;
	}

	/**
	 * 获取Http head信息
	 * 
	 * @param http
	 * @return
	 */
	public static HashMap<String, String> getHeader(HttpURLConnection http) {
		// long totalLength = 0;
		HashMap<String, String> map = new HashMap<String, String>();
		Map<String, String> header = getHttpResponseHeader(http);
		for (Map.Entry<String, String> entry : header.entrySet()) {
			String key = entry.getKey() != null ? entry.getKey() : "";
			if ("fileSize".equalsIgnoreCase(key)) {
				String length = entry.getValue();
				// if (length != null && !"".equals(length.trim()))
				// totalLength = Long.valueOf(length);
				map.put("fileSize", length);
			} else if ("MD5-value".equalsIgnoreCase(key)) {
				String md5Value = entry.getValue();
				// if (md5Value != null && !"".equals(md5Value.trim()))
				// totalLength = Long.valueOf(md5Value);
				map.put("MD5-value", md5Value);
			}
		}
		return map;
	}

	/**
	 * 获取Http响应头字段
	 * 
	 * @param http
	 * @return
	 */
	private static Map<String, String> getHttpResponseHeader(
			HttpURLConnection http) {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0;; i++) {
			String mine = http.getHeaderField(i);
			if (mine == null)
				break;
			header.put(http.getHeaderFieldKey(i), mine);
		}
		return header;
	}
}
