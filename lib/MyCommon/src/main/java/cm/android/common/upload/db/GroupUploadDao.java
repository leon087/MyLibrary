package cm.android.common.upload.db;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

import cm.android.common.db.BaseDao;

public class GroupUploadDao extends BaseDao<GroupUploadBean> {

    /**
     * 更新数据
     */
    public CreateOrUpdateStatus saveOrUpdate(GroupUploadBean model) {
        // String querySql = "contentId='" + model.getContentId() + "'";
        return super.insertOrUpdate(model, "contentId", model.getContentId());
    }

    /**
     * 删除数据
     */
    public int delete(GroupUploadBean model) {
        return super.delete("contentId", model.getContentId());
    }

}
