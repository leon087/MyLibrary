package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public final class ServiceHolder {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final HashMap<String, Object> mServices = new HashMap<String, Object>();

    public ServiceHolder() {
    }

    /**
     * 初始化管理模块
     */
    public void addService(String name, Object manager) {
        Object tmp = mServices.get(name);
        if (tmp != null) {
            logger.error("name = {},manager = {},tmp = {}", name, manager, tmp);
            return;
        }
        mServices.put(name, manager);
    }

    // public Object getService(Class<?> clazz) {
    // return mServices.get(clazz.getSimpleName());
    // }

    /**
     * 获取单例服务对象
     */
    public <T> T getService(String name) {
        return (T) mServices.get(name);
    }

    public void resetService() {
        mServices.clear();
    }


    public boolean isEmpty() {
        return mServices.isEmpty();
    }
}
