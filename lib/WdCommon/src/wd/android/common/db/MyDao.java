package wd.android.common.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import wd.android.util.util.MyLog;
import wd.android.util.util.ObjectUtil;

public class MyDao<T, K> {
	protected static final int ERROR = -1;
	protected Dao<T, K> dao;

	/**
     *
     */
	public MyDao() {
	}

	public MyDao(Dao<T, K> dao) {
		this.dao = dao;
	}

	// 主要方法:
	//
	// create:插入一条数据
	//
	// createIfNotExists:如果不存在则插入
	//
	// createOrUpdate:如果指定id则更新
	//
	// queryForId:更具id查找
	//
	// update 查找出数据
	//
	// refresh的解释:If you want to use other elds in the Account, you must call
	// refresh on the accountDao class to get the Account object lled in.
	//
	// delte 删除数据
	//
	// queryBuilder() 创建一个查询生成器:进行复杂查询
	//
	// deleteBuilder() 创建一个删除生成器,进程复杂条件删除
	//
	// updateBuilder() 创建修条件生成器,进行复杂条件修改
	//
	// 条件查找器DeleteBuilder,QueryBuilder,UpdateBuilder
	//
	// 查找器是帮助拼接条件语句的.比如查找器中有 where()方法 and()方法 eq()方法 lt()方法 qt()方法
	// between方法这些方法很直观..很容易都明了什么意思
	//
	// 最后使用prepare()方法生成条件使用DAO.query || DAO.delete|| DAO.update 方法执行
	//
	// 可以使用查找生成器QueryBuilder 的 orderby limit offset 方法进行排序,分页,

	public int insert(T bean) {
		try {
			// dao.createIfNotExists(bean);
			return dao.create(bean);
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}

		return ERROR;
	}

	public CreateOrUpdateStatus insertOrUpdate(T bean) {
		try {
			return dao.createOrUpdate(bean);
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}

		return new CreateOrUpdateStatus(false, false, ERROR);
	}

	// public int update(String fieldName, Object value) {
	// try {
	// UpdateBuilder<T, K> updateBuilder = dao.updateBuilder();
	// updateBuilder.where().eq(fieldName, value);
	// return updateBuilder.update();
	// } catch (SQLException e) {
	// MyLog.e(e);
	// } catch (NullPointerException e) {
	// MyLog.e(e);
	// }
	// return ERROR;
	// }

	// public int update(T bean) {
	// try {
	// return dao.update(bean);
	// } catch (SQLException e) {
	// MyLog.e(e);
	// } catch (NullPointerException e) {
	// MyLog.e(e);
	// }
	// return ERROR;
	// }

	public int delete(T bean) {
		try {
			return dao.delete(bean);
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}
		return ERROR;
	}

	public int deleteAll() {
		try {
			return dao.delete(queryAll());
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}
		return ERROR;
	}

	public int delete(String fieldName, Object value) {
		try {
			DeleteBuilder<T, K> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq(fieldName, value);
			return deleteBuilder.delete();
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}
		return ERROR;
	}

	public List<T> queryAll() {
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}
		return ObjectUtil.newArrayList();
	}

	public List<T> query(Map<String, Object> map) {
		try {
			return dao.queryForFieldValues(map);
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}
		return ObjectUtil.newArrayList();
	}

	public List<T> query(String fieldName, Object value) {
		try {
			// 类似 select * from User where 'username' = username;
			// List<User> users = dao.queryBuilder().where().eq("username",
			// username).query();
			// List<Account> accountList = accountDao.query(accountDao
			// .queryBuilder().where()
			// .eq(Account.PASSWORD_FIELD_NAME, "qwerty").prepare());
			return dao.queryForEq(fieldName, value);
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}
		return ObjectUtil.newArrayList();
	}

	public T queryForFirst(String columnName, Object value) {
		try {
			QueryBuilder<T, K> queryBuilder = dao.queryBuilder();
			queryBuilder.where().eq(columnName, value);
			PreparedQuery<T> pq = queryBuilder.prepare();
			return dao.queryForFirst(pq);
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}

		return null;
	}

	public T query(K key) {
		try {
			return dao.queryForId(key);
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}
		return null;
	}

	public long getCount() {
		try {
			return dao.countOf();
		} catch (SQLException e) {
			MyLog.e(e);
		} catch (NullPointerException e) {
			MyLog.e(e);
		}
		return 0;
	}

}
