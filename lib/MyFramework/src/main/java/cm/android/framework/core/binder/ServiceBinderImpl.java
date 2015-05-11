package cm.android.framework.core.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.framework.core.IServiceManager;

public final class ServiceBinderImpl extends cm.android.framework.core.IServiceBinder.Stub {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final AtomicBoolean isInitAtomic = new AtomicBoolean(false);

    private final ServiceHolder serviceHolder = new ServiceHolder();

    private IServiceManager serviceManager;

    private Context context;

    public void initialize(Context context) {
        this.context = context;
        reset();
    }

    public void release() {
        try {
            destroy();
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
        reset();
        this.context = null;
    }

    private void reset() {
        isInitAtomic.set(false);
        serviceHolder.resetService();
        serviceManager = null;
    }

    public void initService(String serviceName) {
        logger.info("serviceName = {}", serviceName);
        if (serviceManager != null) {
            logger.info("serviceManager = {}", serviceManager);
            return;
        }

        try {
            Class klass = Class.forName(serviceName);
            Constructor constructor = klass.getDeclaredConstructor();
            constructor.setAccessible(true);
            serviceManager = (IServiceManager) constructor.newInstance();
            logger.info("serviceManager = {}", serviceManager);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public final void create() throws RemoteException {
        logger.info("isStarted = " + isInitAtomic.get());
        if (!isInitAtomic.compareAndSet(false, true)) {
            return;
        }

        serviceHolder.resetService();

        if (serviceManager != null) {
            serviceManager.onCreate(context);
        }
    }

    @Override
    public final void destroy() throws RemoteException {
        logger.info("isStarted = " + isInitAtomic.get());
        if (!isInitAtomic.compareAndSet(true, false)) {
            return;
        }

        if (serviceManager != null) {
            serviceManager.onDestroy();
        }
        serviceHolder.resetService();
    }

    @Override
    public final void addService(String name, IBinder binder) throws RemoteException {
        serviceHolder.addService(name, binder);
    }

    @Override
    public final IBinder getService(String name) throws RemoteException {
        return serviceHolder.getService(name);
    }
}
