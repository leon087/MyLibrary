package cm.android.common.upload;

import cm.android.common.upload.db.UploadBean;

public class UploadItem extends BaseUploadItem {

    public int failTimes = 0;

    public UploadBean uploadBean;

    public UploadTask uploadTask;

    public IUpload upload;
}
