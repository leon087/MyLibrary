package wd.android.common.download;

import wd.android.common.download.db.DownloadDao;
import wd.android.common.download.downloader.DefaultDownloader;
import wd.android.util.util.MyLog;

public class DownloadTask implements Runnable {
	private IDownloadListener<DownloadTask> iDownloadListener;

	// private static final long BIGAPP = 1024 * 1024L; // apk block >1M
	// private static final int BUFFER = 1024 * 50;

	// private DownloadBean downloadBean;
	private DownloadItem downloadItem;

	private IDownloader<DownloadTask> downloader;

	// private HttpURLConnection httpConn;
	// private RandomAccessFile randomFile;
	private DownloadDao dao = null;

	public DownloadTask(IDownloadListener<DownloadTask> iDownloadListener,
			DownloadItem item, DownloadDao dao,
			IDownloader<DownloadTask> downloader) {
		this.dao = dao;
		this.iDownloadListener = iDownloadListener;
		// this.downloadBean = downloadBean;
		this.downloadItem = item;
		// this.downloadBean = item.downloadBean;

		if (null == downloader) {
			this.downloader = new DefaultDownloader();
		}
	}

	// public DownloadTask(IDownload iDownload, DownloadItem downloadItem) {
	// this.iDownload = iDownload;
	// // this.downloadBean = downloadBean;
	// this.downloadItem = downloadItem;
	// this.downloadBean = downloadItem.downloadBean;
	// }

	/**
	 * 获取下载bean对象
	 * 
	 * @return
	 */
	public DownloadItem getDownloadItem() {
		return downloadItem;
	}

	// /**
	// * 设置下载状态
	// *
	// * @param status
	// */
	// public void setStatus(int status) {
	// downloadItem.downloadBean.setStatus(status);
	// }

	@Override
	public void run() {
		// 设置状态为正在下载
		if (downloadItem.downloadBean.getStatus() == DownloadStatus.COMPLETED) {
			iDownloadListener.onDownloadCompleted(this);
			return;
		}

		downloadItem.downloadBean.setStatus(DownloadStatus.RUNNING);
		iDownloadListener.onDownloadStart(this);
		try {
			// 执行下载
			boolean flag = downloader.download(this, iDownloadListener);
			if (flag) {
				// 下载完成
				downloadItem.downloadBean.setStatus(DownloadStatus.COMPLETED);
				iDownloadListener.onDownloadCompleted(this);
			} else {
				if ((downloadItem.isDeleted())) {
					downloadItem.downloadBean
							.setStatus(DownloadStatus.CANCELED);
				} else if (downloadItem.isStopped()) {
					downloadItem.downloadBean.setStatus(DownloadStatus.STOPPED);
				}
				iDownloadListener.onDownloadStop(this);
			}
		} catch (DownloadException e) {
			downloadItem.downloadBean.setStatus(DownloadStatus.FAILED);
			iDownloadListener.onDownloadFailed(this, e);
			// 下载异常
			MyLog.e(e);
		} finally {
			// 更新数据库
			dao.saveOrUpdate(downloadItem.downloadBean);

			// IoUtil.closeQuietly(randomFile);
			downloader.reset();
			// closeHttpURLConnection(httpConnection);
			MyLog.i("downloadBean.getStatus() = "
					+ downloadItem.downloadBean.getStatus());
			// closeHttpURLConnection(httpConn);
		}
	}

}
