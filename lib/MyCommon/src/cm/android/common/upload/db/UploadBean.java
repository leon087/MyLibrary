package cm.android.common.upload.db;

import cm.android.common.db.BaseBean;
import com.j256.ormlite.field.DatabaseField;

public class UploadBean extends BaseBean {
	//
	// // status
	// public final static int UPLOAD_STATUS_RUNNING = 1;
	// public final static int UPLOAD_STATUS_FAILED = 2;
	// public final static int UPLOAD_STATUS_PAUSED = 3;
	// public final static int UPLOAD_STATUS_FINISHED = 4;
	// public final static int UPLOAD_STATUS_WAITING = 5;
	//
	// public final static int UPLOAD_STATUS_FAILED_CHANGE = 6;
	//
	// // Task status
	// public boolean mIsCanceled = false;
	// public boolean mIsDeled = false;
	// public boolean mIsOver = false;
	// public boolean mIsFailed = false;
	//
	// // content type for ringtone
	// public static final int CONTENT_TYPE_RINGTONE = -100;
	//
	// public int failTimes = 0;
	// public String status = "01";

	@DatabaseField
	public int status;// 状态
	@DatabaseField
	public String url;// 上传地址
	@DatabaseField
	public String showName;// 上传文件名
	@DatabaseField
	public String uploadFilePath;// 上传文件路径
	@DatabaseField
	public float progress;// 进度
	@DatabaseField
	public String uploadParam;// 服务器需要的参数
	@DatabaseField
	private String contentId;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getUploadFilePath() {
		return uploadFilePath;
	}

	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public String getUploadParam() {
		return uploadParam;
	}

	public void setUploadParam(String uploadParam) {
		this.uploadParam = uploadParam;
	}
}
