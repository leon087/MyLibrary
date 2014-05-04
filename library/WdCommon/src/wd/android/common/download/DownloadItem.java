package wd.android.common.download;

import wd.android.common.download.db.DownloadBean;

public class DownloadItem extends BaseDownloadItem {
	/** 数据库bean */
	public DownloadBean downloadBean;

	/**
	 * 下载速度
	 */
	public long speed;

	public DownloadTask task;
}
