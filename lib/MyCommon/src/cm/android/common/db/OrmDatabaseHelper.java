package cm.android.common.db;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import cm.android.util.MyLog;
import cm.android.util.ObjectUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Set;

/**
 * 数据库包装类
 * 
 */
final class OrmDatabaseHelper extends OrmLiteSqliteOpenHelper {
	private final static Set<Class<? extends BaseBean>> sTables = ObjectUtil
			.newHashSet();

	public OrmDatabaseHelper(Context context) {
		super(context, context.getPackageName() + ".db", null,
				DatabaseConfig.DB_VERSION);
		if (DatabaseConfig.TABLE_LIST != null) {
			sTables.addAll(DatabaseConfig.TABLE_LIST);
		}
		MyLog.i("version = " + DatabaseConfig.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			for (Class<?> clazz : sTables) {
				TableUtils.createTable(connectionSource, clazz);
			}
		} catch (java.sql.SQLException e) {
			MyLog.e(e);
		}
	}

	@TargetApi(11)
	public void onDowngrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		updateDatabase(database, connectionSource, oldVersion, newVersion);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		updateDatabase(database, connectionSource, oldVersion, newVersion);
	}

	private void updateDatabase(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		MyLog.i("newVersion = " + newVersion + ",oldVersion = " + oldVersion);
		if (newVersion != oldVersion) {
			try {
				for (Class<?> clazz : sTables) {
					TableUtils.dropTable(connectionSource, clazz, true);
					TableUtils.createTable(connectionSource, clazz);
				}
			} catch (java.sql.SQLException e) {
				MyLog.e(e);
			}
			// onCreate(database, connectionSource);
		}
	}

	@Override
	public void close() {
		super.close();
		// sDaoMap.clear();
	}

	public <T, K> Dao<T, K> getOrmDao(Class<T> beanClazz) {
		try {
			return super.getDao(beanClazz);
		} catch (SQLException e) {
			MyLog.e(e);
			return null;
		}
	}

	// public <T, K> MyDao<T, K> getDaoWrapper(Class<T> beanClazz) {
	// MyDao<T, K> daoWrapper = sDaoMap.get(beanClazz);
	// if (daoWrapper == null) {
	// Dao<T, K> dao = null;
	// try {
	// dao = super.getDao(beanClazz);
	// } catch (SQLException e) {
	// MyLog.e(e);
	// }
	// daoWrapper = new MyDao<T, K>(dao);
	// sDaoMap.put(beanClazz, daoWrapper);
	// }
	// return daoWrapper;
	// }
}
