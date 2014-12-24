package cm.android.common.upload.group;

import java.util.List;

import cm.android.common.upload.BaseUploadItem;
import cm.android.common.upload.UploadItem;
import cm.android.common.upload.db.GroupUploadBean;

public class GroupUploadItem extends BaseUploadItem {

    // 大任务，可以包含一个或多个UploadTask
    private List<UploadItem> items;

    public GroupUploadBean bean;

    public GroupUploadTask groupTask;

    public List<UploadItem> getItems() {
        return items;
    }

    public void setItems(List<UploadItem> items) {
        this.items = items;
    }

    @Override
    public void start() {
        super.start();
        synchronized (items) {
            for (UploadItem item : items) {
                item.start();
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        synchronized (items) {
            for (UploadItem item : items) {
                item.stop();
            }
        }
    }

    @Override
    public void delete() {
        super.delete();
        synchronized (items) {
            for (UploadItem item : items) {
                item.delete();
            }
        }
    }
}
