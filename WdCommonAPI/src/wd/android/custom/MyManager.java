package wd.android.custom;

import wd.android.app.global.Tag;
import wd.android.app.global.UrlData;
import wd.android.common.db.BaseDao;
import wd.android.common.db.MyDaoManager;
import wd.android.common.image.ImageManager;
import wd.android.framework.global.AccountData;
import wd.android.framework.global.CommonTag;
import wd.android.framework.global.DirData;
import wd.android.framework.global.GlobalData;
import wd.android.framework.manager.ServiceHolder;
import wd.android.util.global.MyPreference;

public class MyManager {

	public static GlobalData getGlobalData() {
		return ServiceHolder.getService(GlobalData.class);
	}

	public static ImageManager getAsyncImageManager() {
		return ServiceHolder.getService(ImageManager.class);
	}

	public static DirData getDirData() {
		return ServiceHolder.getService(DirData.class);
	}

	public static MyPreference getMyPreference() {
		return ServiceHolder.getService(MyPreference.class);
	}

	public static <T extends BaseDao<?>> T getDao(Class<T> daoClazz) {
		return ServiceHolder.getService(MyDaoManager.class).getDao(daoClazz);
	}

	// public static <T, K> MyDao<T, K> getMyDao(Class<T> beanClazz) {
	// return BaseManager.getService(MyDaoManager.class).getMyDao(beanClazz);
	// }

	public static AccountData getAccountData() {
		return getData(CommonTag.ACCOUNT_DATA);
	}

	public static <T> T getData(String tag) {
		return ServiceHolder.getService(GlobalData.class).getData(tag);
	}

	public static <T> void putData(String tag, T value) {
		ServiceHolder.getService(GlobalData.class).putData(tag, value);
	}

	public static String getUrl(String key) {
		UrlData urlData = getData(Tag.URL_DATA);
		return urlData.getUrl(key);
	}

	// public static ThreadPool getThreadPool() {
	// return BaseManager.getService(ThreadPool.class);
	// }
}
