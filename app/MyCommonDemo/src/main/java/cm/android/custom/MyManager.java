package cm.android.custom;

import cm.android.app.global.Tag;
import cm.android.app.global.UrlData;
import cm.android.common.image.ImageManager;
import cm.android.framework.core.global.GlobalData;
import cm.android.framework.core.manager.ServiceHolder;
import cm.android.global.MyPreference;

public class MyManager {

    public static GlobalData getGlobalData() {
        return ServiceHolder.getService(GlobalData.class);
    }

    public static ImageManager getAsyncImageManager() {
        return ServiceHolder.getService(ImageManager.class);
    }

    public static MyPreference getMyPreference() {
        return ServiceHolder.getService(MyPreference.class);
    }

    // public static <T extends BaseDao<?>> T getDao(Class<T> daoClazz) {
    // return BaseManager.getService(MyDaoManager.class).getDao(daoClazz);
    // }

    // public static <T, K> MyDao<T, K> getMyDao(Class<T> beanClazz) {
    // return BaseManager.getService(MyDaoManager.class).getMyDao(beanClazz);
    // }

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
