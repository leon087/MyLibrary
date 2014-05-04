package wd.android.common.db;

import java.util.Collection;
import java.util.Set;

import wd.android.util.util.ObjectUtil;

public class DatabaseConfig {
	/**
	 * 数据库版本号
	 */
	static int DB_VERSION = 1;

	/**
	 * 数据库表集合
	 */
	static final Set<Class<? extends BaseBean>> TABLE_LIST = ObjectUtil
			.newHashSet();

	private static void initVersion(int version) {
		if (version > DB_VERSION) {
			DB_VERSION = version;
		}
	}

	/**
	 * 初始化数据库
	 * 
	 * @param version
	 *            数据库版本号
	 * @param beanClazzs
	 *            table集合
	 */
	public static void initDatabase(int version,
			Collection<Class<? extends BaseBean>> beanClazzs) {
		initVersion(version);
		TABLE_LIST.addAll(beanClazzs);
	}

	public static void initDatabase(int version,
			Class<? extends BaseBean> beanClazzs) {
		initVersion(version);
		TABLE_LIST.add(beanClazzs);
	}
}
