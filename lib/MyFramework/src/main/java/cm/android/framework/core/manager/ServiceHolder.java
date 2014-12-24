package cm.android.framework.core.manager;

import java.util.HashMap;

public final class ServiceHolder {

    private static final HashMap<String, Object> mServices = new HashMap<String, Object>();

    private static final HashMap<String, Object> appService = new HashMap<String, Object>();

    private ServiceHolder() {
    }

    /**
     * 初始化管理模块
     */
    static void addService(Object manager) {
        mServices.put(manager.getClass().getSimpleName(), manager);
    }

    static void addAppService(Object manager) {
        appService.put(manager.getClass().getSimpleName(), manager);
    }

    // public Object getService(Class<?> clazz) {
    // return mServices.get(clazz.getSimpleName());
    // }

    /**
     * 获取单例服务对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> clazz) {
        if (mServices.containsKey(clazz.getSimpleName())) {
            return (T) mServices.get(clazz.getSimpleName());
        } else {
            return (T) appService.get(clazz.getSimpleName());
        }
    }

    static void resetService() {
        mServices.clear();
    }

    static void resetAppService() {
        appService.clear();
    }

    public static boolean isEmpty() {
        return mServices.isEmpty() && appService.isEmpty();
    }
}
