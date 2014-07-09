package cm.android.common.upload.uploader;

import cm.android.common.upload.*;
import cm.android.util.IoUtil;
import cm.android.util.MapUtil;
import cm.android.util.MyLog;
import cm.android.util.Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class DefaultUploader implements IUploader<UploadTask> {
	private static final int BUFFER = 1024 * 32;
	private static final int RETRY = 5;

	private IUpload upload;
	public RandomAccessFile randomFile;

	/**
	 * 重试次数
	 */
	private int retryCount = 0;

	public DefaultUploader(IUpload upload) {
		this.upload = upload;
	}

	// 封装一个Http连接
	public HttpURLConnection getUploadConnection(String reqUrl,
			Map<String, String> parameters) throws UploadException {
		if (MyLog.isDebug()) {
			MyLog.d(reqUrl);
		}

		try {
			HttpURLConnection mHttpConn = null;
			URL url = new URL(reqUrl);
			// 设置代理
			mHttpConn = (HttpURLConnection) url.openConnection();
			// if (GlobalSetting.CURRENTNETTYPE == GlobalSetting.WAP) {
			// System.getProperties().put("proxySet", "true");
			// System.getProperties().put("http.proxyHost",
			// GlobalSetting.CMCC_WAP_PROXY_HOST);
			// System.getProperties().put("http.proxyPort",
			// GlobalSetting.CMCC_WAP_PROXY_PORT);
			// }
			mHttpConn.setUseCaches(false);
			mHttpConn.setRequestMethod("POST");
			mHttpConn.setDoOutput(true);
			mHttpConn.setDoInput(true);
			mHttpConn.setRequestProperty("Content-Type",
					"application/octet-stream");
			// mHttpConn.setRequestProperty("User-Agent",
			// GlobalSetting.userAgent);
			mHttpConn.setRequestProperty("Connection", "Close");
			mHttpConn.setReadTimeout(1000 * 60);
			mHttpConn.setConnectTimeout(1000 * 60);
			// 连接
			mHttpConn.connect();
			return mHttpConn;
		} catch (Exception e) {
			MyLog.e(e);
			throw new UploadException(e);
		}
	}

	/**
	 * 读取文件，分段读取，读一段，上传一段，把一段文件数据扔到upload()核心方法中上传
	 * 
	 * @param fileUrl
	 */
	@Override
	public boolean upload(UploadTask task,
			IUploadListener<UploadTask> iUploadListener) throws UploadException {
		// if (!EnvironmentInfo.isExternalStorageUsable()) {
		// throw new UploadException(
		// UploadException.EXTERNAL_STORAGE_USABLE_ERROR);
		// }

		UploadItem uploadItem = task.getUploadItem();

		// if (uploadItem.uploadBean.getStatus() == UploadLocalStatus.CANCELED)
		// {
		// return false;
		// } else if (uploadItem.uploadBean.getStatus() ==
		// UploadLocalStatus.COMPLETED) {
		// return true;
		// }
		if (!isRunning(uploadItem)) {
			return false;
		}
		// upload之前会判断，此处冗余判断
		else if (uploadItem.uploadBean.getStatus() == UploadStatus.COMPLETED) {
			return true;
		}

		File file = new File(uploadItem.uploadBean.getUploadFilePath());
		if (!file.exists()) {
			// 文件不存在
			uploadItem.uploadBean.setStatus(UploadStatus.FAILED);
			throw new UploadException(UploadException.FILE_NOT_FOUND);
		}

		// UploadHander uploadHander = new UploadHander();

		long start = 0;
		// URL为空则请求上传地址，不为空则获取文件上传的position
		if (Utils.isEmpty(uploadItem.uploadBean.getUrl())) {
			// 请求上传地址
			Map<String, String> uploadUrlMap = upload
					.getUploadURL(uploadItem.uploadBean);
			String status = MapUtil.getString(uploadUrlMap, "STATUS");
			if ("01".equals(status)) {
				// 请求处理成功
			} else if ("13".equals(status)) {
				// 上传完成
				uploadItem.uploadBean.setStatus(UploadStatus.COMPLETED);
				return true;
			} else if (!Utils.isEmpty(status)) {
				// 失败
				uploadItem.uploadBean.setStatus(UploadStatus.FAILED);
				throw new UploadException(UploadException.UNKNOWN);
			}
			// 解析数据
			String uploadUrlParam = MapUtil.getString(uploadUrlMap, "PARAM");
			uploadItem.uploadBean.setUploadParam(uploadUrlParam);
			String uploadUrl = MapUtil.getString(uploadUrlMap, "URL");
			uploadItem.uploadBean.setUrl(uploadUrl);
		} else {
			// 获取上传文件位置
			Map<String, String> lastUploadPosMap = upload
					.getLastUploadPos(uploadItem.uploadBean);
			String status = MapUtil.getString(lastUploadPosMap, "STATUS");
			if ("01".equals(status)) {
				// 请求处理成功
			} else if ("13".equals(status)) {
				// 上传完成
				uploadItem.uploadBean.setStatus(UploadStatus.COMPLETED);
				return true;
			} else if (!Utils.isEmpty(status)) {
				// 失败
				uploadItem.uploadBean.setStatus(UploadStatus.FAILED);
				throw new UploadException(UploadException.UNKNOWN);
			}
			start = MapUtil.getLong(lastUploadPosMap, "SIZE");
		}

		try {
			randomFile = new RandomAccessFile(file, "rw");
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			throw new UploadException(UploadException.FILE_NOT_FOUND, e2);
		}

		byte[] buffer = new byte[BUFFER];
		int length = -1;
		OutputStream ops = null;

		retryCount = 0;
		while (true) {
			int localStatus = uploadItem.uploadBean.getStatus();
			// 设置临时等待状态
			// while (localStatus == UploadLocalStatus.WAITING) {
			// ThreadUtil.sleep(1000);
			// }
			//
			// 非等待并且非上传中则退出
			// if (!(localStatus == UploadLocalStatus.RUNNING)) {
			// return false;
			// }

			// 超过重试次数则抛异常
			if (retryCount >= RETRY) {
				MyLog.e("retryCount = " + retryCount);
				throw new UploadException(UploadException.UNKNOWN);
			}

			if (!isRunning(uploadItem)) {
				return false;
			}

			try {
				randomFile.seek(start);
				// TODO 不知道为什么
				if ((length = randomFile.read(buffer)) == -1) {
					// mItem.mIsOver = true;
					uploadItem.uploadBean.setStatus(UploadStatus.COMPLETED);
					return true;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new UploadException(e1);
			}

			long end = start + length;

			HttpURLConnection httpConnection = null;
			try {
				// 上传文件
				httpConnection = getUploadConnection(
						uploadItem.uploadBean.getUrl() + "?"
								+ uploadItem.uploadBean.getUploadParam()
								+ "&P_START=" + start + "&P_END=" + end, null);
				ops = httpConnection.getOutputStream();
				ops.write(buffer, 0, length);
				ops.flush();

				if (MyLog.isDebug()) {
					MyLog.d("httpConnection.getResponseCode() = "
							+ httpConnection.getResponseCode());
				}

				XMLParserAPN apnreturn = new XMLParserAPN(
						httpConnection.getInputStream());

				String status = apnreturn.getValueByTag("STATUS");
				if ("01".equals(status)) {
					uploadItem.failTimes = 0;
					start = end;
					// 计算上传进度
					float percent = end * 1F / file.length();
					uploadItem.uploadBean.setProgress(percent);
					iUploadListener.onUploading(task, percent);
				} else if ("13".equals(status)) {
					uploadItem.uploadBean.setStatus(UploadStatus.COMPLETED);
					return true;
				} else if (!Utils.isEmpty(status)) {
					MyLog.e("status = " + status);
					uploadItem.uploadBean.setStatus(UploadStatus.FAILED);
					throw new UploadException(UploadException.UNKNOWN);
				}

				// 重置重试次数
				retryCount = 0;
			} catch (IOException e) {
				retryCount++;
				MyLog.e("retryCount = " + retryCount, e);
				// throw new UploadException(e);
			} finally {
				IoUtil.closeQuietly(ops);
				closeHttpURLConnection(httpConnection);
			}
		}
	}

	private boolean isRunning(UploadItem uploadItem) {
		if (uploadItem.isDeleted()) {
			uploadItem.uploadBean.setStatus(UploadStatus.CANCELED);
			return false;
		} else if (uploadItem.isStopped()) {
			uploadItem.uploadBean.setStatus(UploadStatus.STOPPED);
			return false;
		}
		return true;
	}

	private void closeHttpURLConnection(HttpURLConnection httpConn) {
		if (httpConn != null) {
			httpConn.disconnect();
			httpConn = null;
		}
	}

	@Override
	public void reset() {
		IoUtil.closeQuietly(randomFile);
	}
}
