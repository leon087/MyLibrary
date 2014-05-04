package wd.android.common.download.db;

import wd.android.common.db.BaseDao;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

/**
 * 下载数据DAO
 */
public class DownloadDao extends BaseDao<DownloadBean> {

	/**
	 * 更新数据
	 * 
	 * @param model
	 * @return
	 */
	public CreateOrUpdateStatus saveOrUpdate(DownloadBean model) {
		return super.insertOrUpdate(model, "key", model.getKey());
	}

	/**
	 * 删除数据
	 * 
	 * @param model
	 * @return
	 */
	public int delete(DownloadBean model) {
		return super.delete("key", model.getKey());
	}

}
