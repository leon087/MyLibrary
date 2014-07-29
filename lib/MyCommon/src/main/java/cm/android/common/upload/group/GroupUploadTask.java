package cm.android.common.upload.group;

import cm.android.common.db.MyDaoManager;
import cm.android.common.upload.*;
import cm.android.common.upload.db.UploadDao;

import java.util.List;

/**
 * 集合上传，以后有时间完善，把一个集合当作一个内容，表字段：contId，status
 */
public class GroupUploadTask implements Runnable {
    private GroupUploadItem groupItem;
    private UploadDao uploadDao;
    private IUploadListener<GroupUploadTask> groupUploadListener;

    public GroupUploadTask(GroupUploadItem item, MyDaoManager daoManager,
                           IUploadListener<GroupUploadTask> groupUploadListener) {
        groupItem = item;
        uploadDao = daoManager.getDao(UploadDao.class);
        this.groupUploadListener = groupUploadListener;
    }

    public GroupUploadItem getItem() {
        return groupItem;
    }

    @Override
    public void run() {
        // for (UploadItem item : items) {
        // // 调用
        // }
        upload();
    }

    // /**
    // * 用户设置
    // */
    // public void setStatus(int status) {
    // item.bean.setStatus(status);
    // if (item.isDeleted() || item.isStopped()) {
    // List<UploadItem> items = item.getItems();
    // synchronized (items) {
    // for (UploadItem item : items) {
    // item.uploadBean.setStatus(status);
    // }
    // }
    // }
    // }

    private boolean isRunning() {
        if (groupItem.isDeleted()) {
            groupItem.bean.setStatus(UploadStatus.CANCELED);
            return false;
        } else if (groupItem.isStopped()) {
            groupItem.bean.setStatus(UploadStatus.STOPPED);
            return false;
        }
        return true;
    }

    private void upload() {
        groupItem.bean.setStatus(UploadStatus.RUNNING);
        groupUploadListener.onUploadStart(this);

        synchronized (this) {
            List<UploadItem> items = groupItem.getItems();
            for (UploadItem item : items) {
                if (!isRunning()) {
                    return;
                }

                if (item.uploadTask == null) {
                    item.uploadTask = new UploadTask(item, taskUploadListener,
                            uploadDao, null);
                }
                item.uploadTask.run();
                // 如果有一个任务失败，则认为全部失败
                // 如果全部完成则算完成
                int singleStatus = item.uploadTask.getUploadItem().uploadBean
                        .getStatus();
                if (singleStatus == UploadStatus.FAILED) {
                    groupItem.bean.setStatus(UploadStatus.FAILED);
                    groupUploadListener.onUploadFailed(this, null);
                    return;
                }
            }
            groupItem.bean.setStatus(UploadStatus.COMPLETED);
            groupUploadListener.onUploadCompleted(this);
            return;
        }
    }

    public void deleteRecord() {
        synchronized (this) {
            List<UploadItem> items = groupItem.getItems();
            for (UploadItem uploadItem : items) {
                uploadDao.delete(uploadItem.uploadBean);
            }
        }
    }

    private IUploadListener<UploadTask> taskUploadListener = new IUploadListener<UploadTask>() {

        @Override
        public void onUploadStart(UploadTask task) {

        }

        @Override
        public void onUploadStop(UploadTask task) {
        }

        @Override
        public void onUploadFailed(UploadTask task, UploadException e) {
            // 暂停当前queue上传，上传下一个queue
        }

        @Override
        public void onUploadCompleted(UploadTask task) {
            // 下载下一个task
        }

        @Override
        public void onUploading(UploadTask task, float percent) {

        }
    };
}
