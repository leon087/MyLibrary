package cm.android.framework.server;

import android.os.IBinder;

import cm.android.framework.interfaces.IServiceFetcher;
import cm.java.util.Singleton;

public class ServiceFetcherServer extends IServiceFetcher.Stub {
    private ServiceFetcherServer() {
    }

    public static final Singleton<ServiceFetcherServer> singleton = new Singleton<ServiceFetcherServer>() {
        @Override
        protected ServiceFetcherServer create() {
            return new ServiceFetcherServer();
        }
    };

    public static ServiceFetcherServer get() {
        return singleton.get();
    }

    @Override
    public IBinder getService(String name) {
        return ServiceCache.getService(name);
    }

    @Override
    public void addService(String name, IBinder service) {
        ServiceCache.addService(name, service);
    }

    @Override
    public void removeService(String name) {
        ServiceCache.removeService(name);
    }

    @Override
    public void clearService() {
        ServiceCache.clearService();
    }
}
