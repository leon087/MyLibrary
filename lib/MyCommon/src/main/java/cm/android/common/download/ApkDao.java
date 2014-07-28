package cm.android.common.download;

import cm.android.common.db.BaseDao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

public class ApkDao extends BaseDao<ApkBean> {
    public int deleteByDownloadId(long downloadId) {
        return super.delete("downloadId", downloadId);
    }

    public ApkBean queryByDownloadId(long downloadId) {
        return super.queryForFirst("downloadId", downloadId);
    }

    public ApkBean queryByPackageName(String packageName) {
        return super.queryForFirst("packageName", packageName);
    }

    @Override
    public CreateOrUpdateStatus insertOrUpdate(ApkBean bean) {
        return super.insertOrUpdate(bean, "downloadId", bean.getDownloadId());
    }
}
