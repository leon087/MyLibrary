package wd.android.common.upload;

public class UploadStatus {
	/** 正在上传 ，用户可控制 */
	public final static int RUNNING = 1;

	/**
	 * 完成
	 */
	public final static int COMPLETED = RUNNING + 1;

	/**
	 * 上传失败
	 */
	public final static int FAILED = RUNNING + 2;

	/**
	 * 等待状态（临时状态）
	 */
	public final static int WAITING = RUNNING + 3;

	/**
	 * 暂停 ，用户可控制
	 */
	public final static int STOPPED = RUNNING + 4;

	/**
	 * 取消上传 ，用户可控制
	 */
	public final static int CANCELED = RUNNING + 5;
}
