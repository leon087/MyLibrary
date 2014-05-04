package wd.android.common.download.db;

import wd.android.common.db.BaseBean;

import com.j256.ormlite.field.DatabaseField;

public class DownloadBean extends BaseBean {
	/**
	 * 下载文件名
	 */
	@DatabaseField
	private String name = "";

	/**
	 * 下载文件绝对路径
	 */
	@DatabaseField
	private String filePath = null;

	/**
	 * 已经下载的文件块大小
	 */
	@DatabaseField
	private long downLength = 0l;

	/**
	 * 下载文件总大小
	 */
	@DatabaseField
	private long totalLength = 0l;

	/**
	 * 下载地址
	 */
	@DatabaseField
	private String downloadUrl;
	@DatabaseField
	private String key;

	// /**
	// * 下载对象请求地址
	// */
	// private String requestUrl;

	/**
	 * 图片地址
	 */
	@DatabaseField
	private String iconUrl = "";

	/**
	 * 下载文件类型
	 */
	@DatabaseField
	private String fileType;
	@DatabaseField
	private String md5;
	@DatabaseField
	private int status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getDownLength() {
		return downLength;
	}

	public void setDownLength(long downLength) {
		this.downLength = downLength;
	}

	public long getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(long totalLength) {
		this.totalLength = totalLength;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
