package wd.android.common.download;

import wd.android.common.upload.UploadException;

public interface IDownloader<T> {
	/**
	 * 上传核心方法
	 * 
	 * @return
	 * @throws UploadException
	 */
	boolean download(T task, IDownloadListener<T> listener)
			throws DownloadException;

	void reset();
}
