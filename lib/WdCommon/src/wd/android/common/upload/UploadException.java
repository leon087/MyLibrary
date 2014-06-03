package wd.android.common.upload;

public class UploadException extends Exception {

	private static final long serialVersionUID = 3389168874071480730L;

	public static final String EXTERNAL_STORAGE_USABLE_ERROR = "sdcard卡不可用";
	public static final String FILE_NOT_FOUND = "上传文件不存在";
	public static final String UNKNOWN = "未知异常";

	public UploadException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public UploadException(String detailMessage) {
		super(detailMessage);
	}

	public UploadException(Throwable throwable) {
		super(throwable);
	}

}
