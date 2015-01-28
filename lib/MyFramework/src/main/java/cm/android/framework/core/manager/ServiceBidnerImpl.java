package cm.android.framework.core.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Binder;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServiceBidnerImpl extends Binder implements IServiceBinder {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final AtomicBoolean isInitAtomic = new AtomicBoolean(false);

    private final ServiceHolder serviceHolder = new ServiceHolder();

    private IServiceManager serviceManager;

    @Override
    public void initService(IServiceManager serviceManager) {
        if (serviceManager == null) {
            throw new IllegalArgumentException("serviceManger = null");
        }

        if (this.serviceManager == null) {
            this.serviceManager = serviceManager;
        }
    }

    @Override
    public final void create() {
        logger.info("isInit = " + isInitAtomic.get());
        if (!isInitAtomic.compareAndSet(false, true)) {
            return;
        }

        serviceHolder.resetService();
        serviceManager.onCreate();
    }

    @Override
    public final void destroy() {
        logger.info("isInit = " + isInitAtomic.get());
        if (!isInitAtomic.compareAndSet(true, false)) {
            return;
        }

        serviceManager.onDestroy();
        serviceHolder.resetService();
    }

    @Override
    public final void addService(String name, Object manager) {
        serviceHolder.addService(name, manager);
    }

    @Override
    public final <T> T getService(String name) {
        return serviceHolder.getService(name);
    }
}
