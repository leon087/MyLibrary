package wd.android.common.download.downloader;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import wd.android.common.download.DownloadException;
import wd.android.common.download.DownloadItem;
import wd.android.common.download.DownloadStatus;
import wd.android.common.download.DownloadTask;
import wd.android.common.download.DownloadUtils;
import wd.android.common.download.IDownloadListener;
import wd.android.common.download.IDownloader;
import wd.android.common.download.db.DownloadBean;
import wd.android.util.thread.ThreadUtil;
import wd.android.util.util.EnvironmentInfo;
import wd.android.util.util.IoUtil;
import wd.android.util.util.Md5Util;
import wd.android.util.util.MyLog;

public class WonderDownloader implements IDownloader<DownloadTask> {
	private static final int BUFFER = 1024 * 50;
	private static final int RETRY = 5;

	private RandomAccessFile randomFile;
	private HttpURLConnection httpConn;
	private FileSizeHolder mFileSizeHolder = new FileSizeHolder();

	// /**
	// * 重试次数
	// */
	// private int retryCount = 0;

	public WonderDownloader() {
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

		// String downloadUrl = download.getDownloadURL(downloadItem);
		// downloadItem.downloadBean.setDownloadUrl(downloadUrl);

		// item.setState(DownloadItem.DOWNLOAD_STATUS_RUNNING);
		// 下载文件的总大小
		// long totalSize = downloadBean.getTotalLength();
		long totalSize = mFileSizeHolder
				.getTotalSize(downloadItem.downloadBean);
		if (totalSize <= 0) {
			downloadItem.downloadBean.setStatus(DownloadStatus.FAILED);
			MyLog.e("totalSize = " + totalSize);
			return false;
		}

		// 文件对象
		File saveFile = new File(downloadItem.downloadBean.getFilePath());
		// 分块块大小
		// long blockSize = 0;
		// 已经下载的大小
		long downloadLength = downloadItem.downloadBean.getDownLength();
		randomFile = getRandomAccessFile(saveFile, totalSize);

		httpConn = getHttpURLConnection(downloadItem.downloadBean
				.getDownloadUrl());

		while (downloadLength < totalSize) {
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

				byte[] buf = new byte[BUFFER];
				int size = 0;
				while ((size = is.read(buf, 0, BUFFER)) != -1) {
					randomFile.write(buf, 0, size);
					downloadLength += size;
				}
				downloadItem.downloadBean.setDownLength(downloadLength);
				// // 更新数据库
				// dao.saveOrUpdate(downloadBean);
				// 保留小数点后两位
				float progress = (downloadLength * 100F / totalSize) / 100F;
				listener.onDownloading(task, progress);
			} catch (IOException e) {
				// retryCount++;
				// MyLog.e("retryCount = " + retryCount, e);
				MyLog.e(e);
				throw new DownloadException(DownloadException.FILE_NOT_FOUND, e);
			} finally {
				IoUtil.closeQuietly(is);
			}
		}

		if (downloadLength >= totalSize) {
			String netMd5 = downloadItem.downloadBean.getMd5();
			String md5 = Md5Util.md5sum(saveFile);
			if (netMd5 != null && netMd5.equalsIgnoreCase(md5)) {
				// iDownload.onDownloadCompleted(downloadBean);
				return true;
			} else {
				MyLog.e("md5 = " + md5 + ",netMd5 = " + netMd5);
				throw new DownloadException(DownloadException.MD5);
			}
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

	private RandomAccessFile getRandomAccessFile(File file, long totalSize)
			throws DownloadException {
		try {
			RandomAccessFile randFile = new RandomAccessFile(file, "rws");
			randFile.setLength(totalSize);
			return randFile;
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			throw new DownloadException(DownloadException.FILE_NOT_FOUND);
		} catch (IOException e1) {
			e1.printStackTrace();
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
		if (EnvironmentInfo.hasEnoughSpace(new File(path))) {
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
			e.printStackTrace();
			throw new DownloadException(DownloadException.HTTP_URL_CONNECTION);
		}
	}

	private HttpURLConnection getHttpURLConnection(String downloadUrl)
			throws DownloadException {
		try {
			URL url = new URL(downloadUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.e(e);
			throw new DownloadException(DownloadException.HTTP_URL_CONNECTION);
		}
	}

	private class FileSizeHolder {
		private long getTotalSize(DownloadBean downloadBean)
				throws DownloadException {
			long totalSize = downloadBean.getTotalLength();
			if (totalSize <= 0) {
				MyLog.e("totalSize = " + totalSize);
				// 获取文件大小
				initFileSize(downloadBean);
			}
			return totalSize;
		}

		public HashMap<String, String> getFileHeader(String downloadUrl)
				throws DownloadException {
			HttpURLConnection httpConnection = null;
			try {
				URL url = new URL(downloadUrl);
				MyLog.i("url = " + url);
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setReadTimeout(1000 * 30);
				httpConnection.setConnectTimeout(1000 * 30);
				httpConnection.setRequestProperty("Charset", "UTF-8");
				httpConnection.setRequestProperty("Range", "bytes=" + 0 + "-"
						+ 1024);
				int code = httpConnection.getResponseCode();
				MyLog.d("code = " + code);
				if (code == HttpURLConnection.HTTP_PARTIAL
						|| code == HttpURLConnection.HTTP_OK) {
					return DownloadUtils.getHeader(httpConnection);
				} else if (code == HttpURLConnection.HTTP_NOT_FOUND
						|| code == HttpURLConnection.HTTP_BAD_REQUEST) {
					throw new DownloadException(DownloadException.URL_INVALID);
				} else if (code == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
					throw new DownloadException(
							DownloadException.REQUEST_TIMEOUT);
				} else if (code == 999) {
					throw new DownloadException(
							DownloadException.DOWNLOAD_URL_INVALID);
				} else {
					throw new DownloadException(
							DownloadException.CODE_EXCEPTION);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				MyLog.e(e);
				throw new DownloadException(DownloadException.OTHER, e);
			} catch (EOFException e) {
				MyLog.e(e);
				throw new DownloadException(e.getMessage(), e);
			} catch (IOException e) {
				e.printStackTrace();
				MyLog.e(e);
				throw new DownloadException(DownloadException.NETWORK_ERROR, e);
			} finally {
				closeHttpURLConnection(httpConnection);
			}
		}

		private HashMap<String, String> getHeader(String url)
				throws DownloadException {
			// 服务器端要写日志(客户端下载时会发两次请求,本次请求服务端要做处理,不写入日志)
			String serverLog = "=getSize";
			String urlStr = url + serverLog;
			try {
				return getFileHeader(urlStr);
			} catch (DownloadException e) {
				// EOFException
				if (-1 != e.getMessage().indexOf("EOF")) {
					// if (e.getCause() instanceof EOFException) {
					try {
						return getFileHeader(urlStr);
					} catch (DownloadException ex) {
						ex.printStackTrace();
						throw e;
					}
				} else {
					throw e;
				}
			}
		}

		public void initFileSize(DownloadBean downloadBean)
				throws DownloadException {
			long filesizeLong = 0L;
			HashMap<String, String> header = getHeader(downloadBean
					.getDownloadUrl());
			if (header == null) {
				throw new DownloadException(DownloadException.FILE_SIZE_ERROR);
			}
			String filesize = header.get("fileSize");

			if (filesize != null && !"".equals(filesize.trim())) {
				filesizeLong = Long.valueOf(filesize);
			}

			if (filesizeLong == 0L) {
				// mHandler.sendMessage(mHandler.obtainMessage(
				// MSG_DOWNLOAD_FILE_SIZE_ZERO_ERROR, item));
				// error(item);
				throw new DownloadException(DownloadException.FILE_SIZE_ERROR);
			}

			String md5Value = header.get("MD5-value");
			if (md5Value == null || "".equals(md5Value.trim())) {
				// mHandler.sendMessage(mHandler.obtainMessage(
				// MSG_DOWNLOAD_FILE_MD5_ERROR, item));
				// // 缺少md5
				// error(item);
				throw new DownloadException(DownloadException.MD5);
			}

			// item.setDataLength(filesizeLong);
			// item.setMd5(md5Value);
			// runTask(item);
			downloadBean.setTotalLength(filesizeLong);
			downloadBean.setMd5(md5Value);
		}
	}

	// /**
	// * 获取下载流
	// *
	// * @param startPos
	// * 文件偏移的开始位置
	// * @param packagesize
	// * 要下载的块的大小
	// * @return
	// * @throws IOException
	// */
	// private InputStream getInputStream(HttpURLConnection httpURLConnection,
	// long startPos, long packagesize) throws IOException {
	// InputStream is = null;
	// try {
	// httpURLConnection.setReadTimeout(1000 * 30);
	// httpURLConnection.setConnectTimeout(1000 * 30);
	// // 分块下载
	// long endPos = startPos + packagesize;
	// String range = "bytes=" + startPos + "-" + endPos;
	// httpURLConnection.setRequestProperty("Range", range);
	//
	// int code = httpURLConnection.getResponseCode();
	// Log.e("==getInputStream==", "=====code===" + code);
	// if (code == HttpURLConnection.HTTP_PARTIAL
	// || code == HttpURLConnection.HTTP_OK) {
	// is = httpURLConnection.getInputStream();
	// } else if (code == 999) {
	// throw new IOException(
	// DownloadConstants.ERROR_DOWNLOADURL_INVALID);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw new IOException(DownloadConstants.EXCEPTION_HTTPURLCONNECTION);
	// }
	// return is;
	// }
	//
	// private InputStream getInputStream(String downloadUrl, long startPos)
	// throws DownloadException {
	// HttpURLConnection httpConn = getHttpURLConnection(downloadUrl);
	// // 非分块下载
	// try {
	// return getInputStream(httpConn, startPos);
	// } catch (DownloadException e) {
	// closeHttpURLConnection(httpConn);
	// throw e;
	// }
	// }
}
