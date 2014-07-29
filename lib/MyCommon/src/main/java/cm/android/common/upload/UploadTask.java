package cm.android.common.upload;

import cm.android.common.upload.db.UploadDao;
import cm.android.common.upload.uploader.DefaultUploader;
import cm.android.util.MyLog;

public class UploadTask implements Runnable {
    private UploadItem uploadItem;
    private IUploadListener<UploadTask> iUploadListener;
    private UploadDao uploadDao;

    // public RandomAccessFile randomFile;
    // private HttpURLConnection httpConnection;

    private IUploader<UploadTask> uploader;

    public UploadTask(UploadItem item,
                      IUploadListener<UploadTask> iUploadListener, UploadDao uploadDao,
                      IUploader<UploadTask> uploader) {
        uploadItem = item;
        this.iUploadListener = iUploadListener;
        this.uploadDao = uploadDao;
        if (null == uploader) {
            this.uploader = new DefaultUploader(item.upload);
        }
    }

    public UploadItem getUploadItem() {
        return uploadItem;
    }

    public void run() {
        if (uploadItem.uploadBean.getStatus() == UploadStatus.COMPLETED) {
            iUploadListener.onUploadCompleted(this);
            return;
        }

        uploadItem.uploadBean.setStatus(UploadStatus.RUNNING);
        iUploadListener.onUploadStart(this);
        try {
            boolean flag = uploader.upload(this, iUploadListener);
            if (flag) {
                uploadItem.uploadBean.setStatus(UploadStatus.COMPLETED);
                iUploadListener.onUploadCompleted(this);
            } else {
                if ((uploadItem.isDeleted())) {
                    uploadItem.uploadBean.setStatus(UploadStatus.CANCELED);
                } else if (uploadItem.isStopped()) {
                    uploadItem.uploadBean.setStatus(UploadStatus.STOPPED);
                }
                iUploadListener.onUploadStop(this);
            }
        } catch (UploadException e) {
            MyLog.e(e);
            uploadItem.uploadBean.setStatus(UploadStatus.FAILED);
            iUploadListener.onUploadFailed(this, e);
        } finally {
            // 更新数据库
            uploadDao.saveOrUpdate(uploadItem.uploadBean);

            uploader.reset();
            // closeHttpURLConnection(httpConnection);
            MyLog.i("uploadBean.getStatus() = "
                    + uploadItem.uploadBean.getStatus());
        }
    }
}
