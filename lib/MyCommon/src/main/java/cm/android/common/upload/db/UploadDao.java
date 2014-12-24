package cm.android.common.upload.db;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

import cm.android.common.db.BaseDao;

/**
 * 上传数据DAO
 */
public class UploadDao extends BaseDao<UploadBean> {

    /**
     * 更新数据
     */
    public CreateOrUpdateStatus saveOrUpdate(UploadBean model) {
        // String querySql = "uploadFilePath='" + model.getUploadFilePath() +
        // "'";
        return super.insertOrUpdate(model, "uploadFilePath",
                model.getUploadFilePath());
    }

    /**
     * 删除数据
     */
    public int delete(UploadBean model) {
        // String querySql = "uploadFilePath='" + model.getUploadFilePath() +
        // "'";
        return super.delete("uploadFilePath", model.getUploadFilePath());
    }

}
