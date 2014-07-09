package cm.android.common.download;

/**
 * 下载状态监听接口
 */
public interface IDownloadListener<T> {

	/**
	 * 开始下载
	 */
	public void onDownloadStart(T task);

	/**
	 * 下载停止
	 * 
	 * @param item
	 */
	public void onDownloadStop(T task);

	/**
	 * 下载失败
	 */
	public void onDownloadFailed(T task, DownloadException e);

	/**
	 * 下载成功
	 */
	public void onDownloadCompleted(T task);

	/**
	 * 正在下载，
	 * 
	 * @param downloadItem
	 * @param percent
	 *            下载进度，百分制计算
	 */
	public void onDownloading(T task, float percent);
}
