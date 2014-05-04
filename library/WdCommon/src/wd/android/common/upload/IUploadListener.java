package wd.android.common.upload;

public interface IUploadListener<T> {
	// public static final int TASK_START = 0x01;
	// public static final int TASK_STOPPED = 0x01;
	// public static final int TASK_FAILED = 0x01;
	// public static final int TASK_COMPLETE = 0x01;
	// public static final int TASK_RUNNING = 0x01;

	/**
	 */
	public void onUploadStart(T task);

	/**
	 * 
	 */
	public void onUploadStop(T task);

	/**
	 */
	public void onUploadFailed(T task, UploadException e);

	/**
	 */
	public void onUploadCompleted(T task);

	/**
	 * 
	 * 
	 * @param task
	 * @param percent
	 *            上传进度
	 */
	public void onUploading(T task, float percent);
}
