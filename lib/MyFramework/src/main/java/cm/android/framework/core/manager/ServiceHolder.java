package cm.android.framework.core.manager;

import java.util.HashMap;

public final class ServiceHolder {
    private static final HashMap<String, Object> mServices = new HashMap<String, Object>();

    private ServiceHolder() {
    }

    /**
     * 初始化管理模块
     *
     * @param manager
     */
    static void addService(Object manager) {
        mServices.put(manager.getClass().getSimpleName(), manager);
    }

    // public Object getService(Class<?> clazz) {
    // return mServices.get(clazz.getSimpleName());
    // }

    /**
     * 获取单例服务对象
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> clazz) {
        return (T) mServices.get(clazz.getSimpleName());
    }

    static void clear() {
        mServices.clear();
    }
}
