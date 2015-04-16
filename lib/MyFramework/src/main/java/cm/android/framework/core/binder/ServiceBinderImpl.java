package cm.android.framework.core.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.framework.core.IServiceManager;

public final class ServiceBinderImpl extends cm.android.framework.core.IServiceBinder.Stub {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final AtomicBoolean isInitAtomic = new AtomicBoolean(false);

    private final ServiceHolder serviceHolder = new ServiceHolder();

    private IServiceManager serviceManager;

    public void initialize() {
        reset();
    }

    public void release() {
        destroy();
        reset();
    }

    private void reset() {
        isInitAtomic.set(false);
        serviceHolder.resetService();
        serviceManager = null;
    }

    @Override
    public void initService(IServiceManager serviceManager) {
        if (serviceManager == null) {
            throw new IllegalArgumentException("serviceManger = null");
        }

        if (this.serviceManager == null) {
            logger.info("IServiceBinder:initService(IServiceManager):" + serviceManager);
            this.serviceManager = serviceManager;
        }
    }

    @Override
    public final void create() {
        logger.info("isStarted = " + isInitAtomic.get());
        if (!isInitAtomic.compareAndSet(false, true)) {
            return;
        }

        serviceHolder.resetService();
        try {
            serviceManager.onCreate();
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public final void destroy() {
        logger.info("isStarted = " + isInitAtomic.get());
        if (!isInitAtomic.compareAndSet(true, false)) {
            return;
        }

        try {
            serviceManager.onDestroy();
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
        serviceHolder.resetService();
    }

    @Override
    public final void addService(String name, IBinder binder) {
        serviceHolder.addService(name, binder);
    }

    @Override
    public final IBinder getService(String name) {
        return serviceHolder.getService(name);
    }
}
