package cm.android.framework.core.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.framework.core.ServiceHolder;

public class LocalServiceBidnerImpl extends Binder implements ILocalServiceBinder, IInterface {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final AtomicBoolean isInitAtomic = new AtomicBoolean(false);

    private final ServiceHolder serviceHolder = new ServiceHolder();

    private ILocalServiceManager serviceManager;

    private static final java.lang.String DESCRIPTOR
            = "cm.android.framework.core.local.IServiceBinder";

    LocalServiceBidnerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static ILocalServiceBinder asInterface(android.os.IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (((iin != null) && (iin instanceof ILocalServiceBinder))) {
            return ((ILocalServiceBinder) iin);
        }
        return null;
    }

    void initialize() {
        reset();
    }

    void release() {
        destroy();
        reset();
    }

    private void reset() {
        isInitAtomic.set(false);
        serviceHolder.resetService();
        serviceManager = null;
    }

    @Override
    public void initService(ILocalServiceManager serviceManager) {
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

    @Override
    public IBinder asBinder() {
        return this;
    }
}
