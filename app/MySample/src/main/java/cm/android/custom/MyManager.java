package cm.android.custom;

import cm.android.common.http.MyHttp;
import cm.android.common.image.ImageManager;
import cm.android.framework.core.global.GlobalData;
import cm.android.framework.core.manager.ServiceHolder;

public class MyManager {

    public static GlobalData getGlobalData() {
        return GlobalData.getInstance();
    }

    public static ImageManager getAsyncImageManager() {
        return ServiceHolder.getService(ImageManager.class);
    }

    // public static <T extends BaseDao<?>> T getDao(Class<T> daoClazz) {
    // return BaseManager.getService(MyDaoManager.class).getDao(daoClazz);
    // }

    // public static <T, K> MyDao<T, K> getMyDao(Class<T> beanClazz) {
    // return BaseManager.getService(MyDaoManager.class).getMyDao(beanClazz);
    // }

    public static MyHttp getHttp() {
        return ServiceHolder.getService(MyHttp.class);
    }

    public static <T> T getData(String tag) {
        return GlobalData.getInstance().getData(tag);
    }

    public static <T> void putData(String tag, T value) {
        GlobalData.getInstance().putData(tag, value);
    }

    // public static ThreadPool getThreadPool() {
    // return BaseManager.getService(ThreadPool.class);
    // }
}
