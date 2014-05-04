package wd.android.common.download;

import wd.android.common.db.BaseBean;

import com.j256.ormlite.field.DatabaseField;

public class ApkBean extends BaseBean {
	@DatabaseField
	protected long downloadId;
	@DatabaseField
	private String packageName;
	@DatabaseField
	private String imgUrl;
	@DatabaseField
	private String name;
	@DatabaseField
	private String downloadTimes;
	@DatabaseField
	private String linkType;
	@DatabaseField
	private String linkUrl;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDownloadTimes() {
		return downloadTimes;
	}

	public void setDownloadTimes(String downloadTimes) {
		this.downloadTimes = downloadTimes;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public long getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(long downloadId) {
		this.downloadId = downloadId;
	}

}
