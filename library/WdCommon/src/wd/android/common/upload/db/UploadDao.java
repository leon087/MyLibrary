package wd.android.common.upload.db;

import wd.android.common.db.BaseDao;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

/**
 * 上传数据DAO
 */
public class UploadDao extends BaseDao<UploadBean> {
	/**
	 * 更新数据
	 * 
	 * @param model
	 * @return
	 */
	public CreateOrUpdateStatus saveOrUpdate(UploadBean model) {
		// String querySql = "uploadFilePath='" + model.getUploadFilePath() +
		// "'";
		return super.insertOrUpdate(model, "uploadFilePath",
				model.getUploadFilePath());
	}

	/**
	 * 删除数据
	 * 
	 * @param model
	 * @return
	 */
	public int delete(UploadBean model) {
		// String querySql = "uploadFilePath='" + model.getUploadFilePath() +
		// "'";
		return super.delete("uploadFilePath", model.getUploadFilePath());
	}

}
