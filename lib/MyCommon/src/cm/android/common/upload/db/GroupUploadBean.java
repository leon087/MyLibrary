package cm.android.common.upload.db;

import cm.android.common.db.BaseBean;
import com.j256.ormlite.field.DatabaseField;

public class GroupUploadBean extends BaseBean {
	@DatabaseField
	private String contentId;
	@DatabaseField
	private int status;

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
}
