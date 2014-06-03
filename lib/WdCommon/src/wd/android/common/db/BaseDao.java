package wd.android.common.db;

import java.lang.reflect.ParameterizedType;

import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

public abstract class BaseDao<T extends BaseBean> extends MyDao<T, Integer> {
	protected Class<T> beanClazz = null;

	public BaseDao() {
		this.beanClazz = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public void setHelper(OrmDatabaseHelper helper) {
		this.dao = helper.getOrmDao(beanClazz);
	}

	/**
	 * 
	 * @param bean
	 * @param uniqueColumnName
	 *            虚拟主键
	 * @param uniqueValue
	 *            虚拟主键值
	 * @return
	 */
	protected CreateOrUpdateStatus insertOrUpdate(T bean,
			String uniqueColumnName, Object uniqueValue) {
		T beanTmp = super.queryForFirst(uniqueColumnName, uniqueValue);
		if (beanTmp != null) {
			// 设置主键
			bean.set_id(beanTmp.get_id());
		}
		return super.insertOrUpdate(bean);
	}
}
