package cm.android.framework.client.ipc;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.BundleCompat;

import java.util.HashMap;

import cm.android.framework.client.core.Framework;
import cm.android.framework.client.core.LogUtil;
import cm.android.framework.interfaces.IServiceFetcher;
import cm.android.framework.server.ServerProvider;
import cm.android.framework.server.ServiceFetcherServer;

public final class ServiceManager {
    private static final HashMap<String, IBinder> sCache = new HashMap<>();
    private static volatile IServiceFetcher sServiceManager;

    private static final IServiceFetcher EMPTY = new IServiceFetcher.Stub() {
        @Override
        public IBinder getService(String name) throws RemoteException {
            LogUtil.getLogger().error("EMPTY:getService():name = {}", name);
            return null;
        }

        @Override
        public void addService(String name, IBinder service) throws RemoteException {
            LogUtil.getLogger().error("EMPTY:addService():name = {},service = {}", name, service);
        }

        @Override
        public void removeService(String name) throws RemoteException {
            LogUtil.getLogger().error("EMPTY:removeService():name = {}", name);
        }

        @Override
        public void clearService() throws RemoteException {
            LogUtil.getLogger().error("EMPTY:clearService()");
        }
    };

    private static IServiceFetcher getIServiceManager() {
        if (sServiceManager != null) {
            return sServiceManager;
        }

        //server进程不用跨进程
        if (Framework.get().isServerProcess()) {
            sServiceManager = ServiceFetcherServer.get();
            return sServiceManager;
        }

        // Find the service manager
//        sServiceManager = ServiceManagerNative.asInterface(BinderInternal.getContextObject());

        Context context = Framework.get().getBaseContext();
        Bundle response = ServerProvider.Proxy.getServiceFetcher(context);
        if (response != null) {
            IBinder binder = BundleCompat.getBinder(response, ServerProvider.KEY_BINDER);
            LocalProxyUtils.linkBinderDied(binder, new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    //TODO ggg server挂掉会重启，无需kill。但是需要重新清下数据
                    sServiceManager = null;
                    sCache.clear();
                }
            });
            sServiceManager = IServiceFetcher.Stub.asInterface(binder);
        }

        return (sServiceManager == null) ? EMPTY : sServiceManager;
    }

    /**
     * Returns a reference to a service with the given name.
     *
     * @param name the name of the service to get
     * @return a reference to the service, or <code>null</code> if the service doesn't exist
     */
    public static IBinder getService(String name) {
        try {
            IBinder service = sCache.get(name);
            if (service != null) {
                return service;
            } else {
                return getIServiceManager().getService(name);
            }
        } catch (RemoteException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Place a new @a service called @a name into the service
     * manager.
     *
     * @param name    the name of the new service
     * @param service the service object
     */
    public static void addService(String name, IBinder service) {
        try {
            getIServiceManager().addService(name, service);
        } catch (RemoteException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
        }
    }

    public static void removeService(String name) {
        sCache.remove(name);

        try {
            getIServiceManager().removeService(name);
        } catch (RemoteException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
        }
    }

    public static void clearService() {
        sCache.clear();

        try {
            getIServiceManager().clearService();
        } catch (RemoteException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
        }
    }
}
