package wd.android.common.db;

import java.lang.reflect.Constructor;
import java.util.Map;

import wd.android.util.util.MyLog;
import wd.android.util.util.ObjectUtil;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

/**
 */
public class MyDaoManager {
	private volatile OrmDatabaseHelper helper;

	private final Map<Class<?>, MyDao> myDaoMap = ObjectUtil.newHashMap();
	public final Map<Class<?>, BaseDao<?>> daoMap = ObjectUtil.newHashMap();

	public MyDaoManager init(Context context) {
		if (helper == null) {
			helper = getHelperInternal(context);
			helper.getWritableDatabase();
		}
		return this;
	}

	public void deInit() {
		daoMap.clear();
		myDaoMap.clear();
		releaseHelper();
		helper.close();
		helper = null;
	}

	/**
	 * 获取dao
	 * 
	 * @param <T>
	 * @param <K>
	 * @param beanClazz
	 * @return
	 */
	public <T, K> MyDao<T, K> getMyDao(Class<T> beanClazz) {
		MyDao<T, K> daoWrapper = myDaoMap.get(beanClazz);
		if (daoWrapper == null) {
			Dao<T, K> dao = helper.getOrmDao(beanClazz);
			daoWrapper = new MyDao<T, K>(dao);
			myDaoMap.put(beanClazz, daoWrapper);
		}
		return daoWrapper;
	}

	/**
	 * 获取dao
	 * 
	 * @param daoClazz
	 * @return
	 */
	public <T extends BaseDao<?>> T getDao(Class<T> daoClazz) {
		T dao = (T) daoMap.get(daoClazz);
		if (dao != null) {
			return dao;
		}

		try {
			Constructor<T> constructor = daoClazz.getDeclaredConstructor(); // 构造函数参数列表的class类型
			constructor.setAccessible(true);
			dao = (T) constructor.newInstance();
		} catch (Exception e) {
			MyLog.e(e);
		}

		if (dao != null) {
			dao.setHelper(helper);
		}
		daoMap.put(daoClazz, (T) dao);
		return dao;
	}

	private OrmDatabaseHelper getHelperInternal(Context context) {
		OrmDatabaseHelper newHelper = (OrmDatabaseHelper) OpenHelperManager
				.getHelper(context, OrmDatabaseHelper.class);
		return newHelper;
	}

	private void releaseHelper() {
		OpenHelperManager.releaseHelper();
	}
}
