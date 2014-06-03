package wd.android.common.download;

import wd.android.common.db.MyDaoManager;
import android.content.Context;

public class ApkDownloadManager extends MyDownloadManager {
	private MyDaoManager daoManager;
	// private MyDao<ApkBean, Long> dao;
	private ApkDao apkDao;

	public void init(Context context) {
		daoManager = new MyDaoManager().init(context);
		// dao = daoManager.getDao(ApkBean.class);
		apkDao = daoManager.getDao(ApkDao.class);
		super.init(context);
	}

	public void deInit() {
		super.deInit();
		daoManager.deInit();
	}

	public long start(String url, ApkBean bean) {
		long id = super.start(url);
		bean.setDownloadId(id);
		// dao.insertOrUpdate(bean);
		apkDao.insertOrUpdate(bean);
		return id;
	}

	public void delete(long... ids) {
		super.delete(ids);
		for (long id : ids) {
			apkDao.deleteByDownloadId(id);
		}
	}

	public void deleteDataAndFile(String packageName) {
		ApkBean apkBean = apkDao.queryByPackageName(packageName);
		if (apkBean != null) {
			long id = apkBean.getDownloadId();
			deleteDataAndFile(id);
		}
	}
}
