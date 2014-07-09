package cm.android.common.upload.db;

import cm.android.common.db.BaseDao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

public class GroupUploadDao extends BaseDao<GroupUploadBean> {

	/**
	 * 更新数据
	 * 
	 * @param model
	 * @return
	 */
	public CreateOrUpdateStatus saveOrUpdate(GroupUploadBean model) {
		// String querySql = "contentId='" + model.getContentId() + "'";
		return super.insertOrUpdate(model, "contentId", model.getContentId());
	}

	/**
	 * 删除数据
	 * 
	 * @param model
	 * @return
	 */
	public int delete(GroupUploadBean model) {
		return super.delete("contentId", model.getContentId());
	}

}
