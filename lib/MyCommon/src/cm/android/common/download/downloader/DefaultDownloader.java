package cm.android.common.download.downloader;

import cm.android.common.download.*;
import cm.android.common.download.db.DownloadBean;
import cm.android.thread.ThreadUtil;
import cm.android.util.EnvironmentInfo;
import cm.android.util.IoUtil;
import cm.android.util.MyLog;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class DefaultDownloader implements IDownloader<DownloadTask> {
	private static final int BUFFER = 1024 * 100;
	private static final int RETRY = 2;

	private RandomAccessFile randomFile;
	private HttpURLConnection httpConn;
	private int retryCount;

	// /**
	// * 重试次数
	// */
	// private int retryCount = 0;

	public DefaultDownloader() {
	}

	private boolean isRunning(DownloadItem downloadItem) {
		if (downloadItem.isDeleted()) {
			downloadItem.downloadBean.setStatus(DownloadStatus.CANCELED);
			return false;
		} else if (downloadItem.isStopped()) {
			downloadItem.downloadBean.setStatus(DownloadStatus.STOPPED);
			return false;
		}
		return true;
	}

	@Override
	public boolean download(DownloadTask task,
			IDownloadListener<DownloadTask> listener) throws DownloadException {
		DownloadItem downloadItem = task.getDownloadItem();

		if (!isRunning(downloadItem)) {
			return false;
		}
		// 之前会判断，此处冗余判断
		else if (downloadItem.downloadBean.getStatus() == DownloadStatus.COMPLETED) {
			return true;
		}

		// 文件对象
		File saveFile = new File(downloadItem.downloadBean.getFilePath());
		// 分块块大小
		// long blockSize = 0;
		// 已经下载的大小
		long downloadLength = downloadItem.downloadBean.getDownLength();
		randomFile = getRandomAccessFile(saveFile);

		httpConn = getHttpURLConnection(downloadItem.downloadBean
				.getDownloadUrl());

		boolean finished = false;
		while (!finished) {
			int status = downloadItem.downloadBean.getStatus();

			// 等待则挂起
			while (status == DownloadStatus.WAITING) {
				ThreadUtil.sleep(1000);
			}

			// // 超过重试次数则抛异常
			// if (retryCount >= RETRY) {
			// MyLog.e("retryCount = " + retryCount);
			// throw new UploadException(UploadException.UNKNOWN);
			// }

			// 非等待并且非下载中则退出
			if (!isRunning(downloadItem)) {
				return false;
			}

			// 初始化位置
			initRandomAccessFile(randomFile, downloadLength);

			// 文件不存在，方法中会抛异常，所以不需要null判断
			validFileIsExist(saveFile);

			// 判断空间，方法中会抛异常，所以不需要null判断
			validSpace(downloadItem);

			InputStream is = null;
			try {
				// 非分块下载
				is = getInputStream(httpConn, downloadLength);
				HeaderHolder.processHeader(httpConn, downloadItem.downloadBean);
				long totalSize = downloadItem.downloadBean.getTotalLength();
				if (totalSize <= 0) {
					downloadItem.downloadBean.setStatus(DownloadStatus.FAILED);
					MyLog.e("totalSize = " + totalSize);
					return false;
				} else if (downloadLength >= totalSize) {
					finished = true;
					break;
				}

				byte[] buf = new byte[BUFFER];
				int size = 0;
				while ((size = is.read(buf, 0, BUFFER)) != -1) {
					randomFile.write(buf, 0, size);
					downloadLength += size;

					downloadItem.downloadBean.setDownLength(downloadLength);
				}

				// // 更新数据库
				// dao.saveOrUpdate(downloadBean);
				// 保留小数点后两位
				float progress = (downloadLength * 100F / downloadItem.downloadBean
						.getTotalLength()) / 100F;
				listener.onDownloading(task, progress);

				if (downloadLength >= totalSize) {
					finished = true;
				}
			} catch (SocketTimeoutException e) {
				retryCount++;
				MyLog.e("retryCount = " + retryCount, e);
				if (retryCount > RETRY) {
					throw new DownloadException(
							DownloadException.HTTP_URL_CONNECTION, e);
				}
			} catch (IOException e) {
				MyLog.e(e);
				throw new DownloadException(DownloadException.OTHER, e);
			} finally {
				IoUtil.closeQuietly(is);
			}
		}

		long totalSize = downloadItem.downloadBean.getTotalLength();
		if (downloadLength >= totalSize) {
			return true;

			// MD5
			// String netMd5 = downloadItem.downloadBean.getMd5();
			// String md5 = Md5Util.md5sum(saveFile);
			// if (netMd5 != null && netMd5.equalsIgnoreCase(md5)) {
			// // iDownload.onDownloadCompleted(downloadBean);
			// return true;
			// } else {
			// MyLog.e("md5 = " + md5 + ",netMd5 = " + netMd5);
			// throw new DownloadException(DownloadException.MD5);
			// }
		} else {
			MyLog.e("downloadLength = " + downloadLength + ",totalSize = "
					+ totalSize);
			return false;
		}
	}

	@Override
	public void reset() {
		IoUtil.closeQuietly(randomFile);
		closeHttpURLConnection(httpConn);
	}

	/**
	 * Moves this file's file pointer to a new position
	 * 
	 * @param randomAccessFile
	 * @param downloadLength
	 * @throws DownloadException
	 */
	private void initRandomAccessFile(RandomAccessFile randomAccessFile,
			long downloadLength) throws DownloadException {
		try {
			randomAccessFile.seek(downloadLength);
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new DownloadException(DownloadException.OTHER, e1);
		}
	}

	private void closeHttpURLConnection(HttpURLConnection httpConn) {
		if (httpConn != null) {
			httpConn.disconnect();
			httpConn = null;
		}
	}

	private RandomAccessFile getRandomAccessFile(File file)
			throws DownloadException {
		try {
			RandomAccessFile randFile = new RandomAccessFile(file, "rws");
			// randFile.setLength(totalSize);
			return randFile;
		} catch (FileNotFoundException e2) {
			MyLog.e(e2);
			throw new DownloadException(DownloadException.FILE_NOT_FOUND);
		} catch (IOException e1) {
			MyLog.e(e1);
			throw new DownloadException(DownloadException.OTHER, e1);
		}
	}

	/**
	 * 校验空间是否足够
	 * 
	 * @throws DownloadException
	 */
	private void validSpace(DownloadItem item) throws DownloadException {
		String path = item.downloadBean.getFilePath();
		if (!EnvironmentInfo.hasEnoughSpace(new File(path))) {
			throw new DownloadException(DownloadException.SDCARD_NOSPACE);
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param file
	 * @throws DownloadException
	 */
	private void validFileIsExist(File file) throws DownloadException {
		if (!file.exists()) {
			MyLog.e("saveFile.exists() = false");
			throw new DownloadException(DownloadException.FILE_NOT_FOUND);
		}
	}

	/**
	 * 获取下载流
	 * 
	 * @param startPos
	 * @return
	 * @throws IOException
	 */
	private InputStream getInputStream(HttpURLConnection httpURLConnection,
			long startPos) throws DownloadException {
		try {
			// mHttpConn = (HttpURLConnection) url.openConnection();
			httpURLConnection.setReadTimeout(1000 * 30);
			httpURLConnection.setConnectTimeout(1000 * 30);
			String range = "bytes=" + startPos + "-";
			httpURLConnection.setRequestProperty("Range", range);
			httpURLConnection.setRequestProperty("Accept-Encoding", "identity");

			int code = httpURLConnection.getResponseCode();
			if (code == HttpURLConnection.HTTP_PARTIAL
					|| code == HttpURLConnection.HTTP_OK) {
				return new BufferedInputStream(
						httpURLConnection.getInputStream());
			} else if (code == 999) {
				throw new DownloadException(
						DownloadException.DOWNLOAD_URL_INVALID);
			} else {
				throw new DownloadException(DownloadException.CODE_EXCEPTION);
			}
		} catch (Exception e) {
			MyLog.e(e);
			throw new DownloadException(DownloadException.HTTP_URL_CONNECTION);
		}
	}

	private HttpURLConnection getHttpURLConnection(String downloadUrl)
			throws DownloadException {
		try {
			URL url = new URL(downloadUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			MyLog.e(e);
			throw new DownloadException(DownloadException.HTTP_URL_CONNECTION);
		}
	}

	private static class HeaderHolder {
		public static void processHeader(HttpURLConnection httpURLConnection,
				DownloadBean bean) throws DownloadException {
			if (httpURLConnection == null) {
				throw new DownloadException(DownloadException.OTHER);
			}

			String headerTransferEncoding = httpURLConnection
					.getHeaderField("Transfer-Encoding");
			if (headerTransferEncoding == null) {
				// fileName
				String totalSize = httpURLConnection
						.getHeaderField("Content-Length");
				bean.setTotalLength(Long.parseLong(totalSize));
				MyLog.i("totalSize = " + totalSize);
			} else {
				MyLog.e("headerTransferEncoding = " + headerTransferEncoding);
				bean.setTotalLength(-1);
				throw new DownloadException(DownloadException.FILE_SIZE_ERROR);
			}

		}
		// header = response.getFirstHeader("Content-Length");
		// if (header != null) {
		// innerState.mHeaderContentLength = header.getValue();
		// mInfo.mTotalBytes = Long
		// .parseLong(innerState.mHeaderContentLength);
		// }
	}

}
