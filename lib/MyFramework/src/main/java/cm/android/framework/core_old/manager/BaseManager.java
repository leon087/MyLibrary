package cm.android.framework.core_old.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

/**
 * Manager基类
 */
public abstract class BaseManager implements IManager {

    private volatile boolean isStart = false;

    private static final Logger logger = LoggerFactory.getLogger(BaseManager.class);

    @Override
    public final synchronized void init(Context context) {
        ServiceHolder.resetAppService();
        onInit(context);
    }

    @Override
    public final synchronized void create() {
        logger.info("isStart = " + isStart);
        if (isStart) {
            return;
        }
        isStart = true;
        ServiceHolder.resetService();
        onCreate();
    }

    @Override
    public final synchronized void destroy() {
        logger.info("isStart = " + isStart);
        if (!isStart) {
            return;
        }
        isStart = false;
        onDestroy();
        ServiceHolder.resetService();
    }

    protected abstract void onInit(Context context);

    /**
     * 初始化资源
     */
    protected abstract void onCreate();

    /**
     * 与onCreate对应，释放资源
     */
    protected abstract void onDestroy();

    /**
     * 初始化管理模块
     */
    protected final void addAppService(Object manager) {
        ServiceHolder.addAppService(manager);
    }

    protected final void addService(Object manager) {
        ServiceHolder.addService(manager);
    }

    // public Object getService(Class<?> clazz) {
    // return mServices.get(clazz.getSimpleName());
    // }
}
