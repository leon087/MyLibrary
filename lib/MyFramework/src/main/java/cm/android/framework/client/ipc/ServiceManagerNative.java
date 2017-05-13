package cm.android.framework.client.ipc;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.BundleCompat;

import cm.android.framework.client.core.Framework;
import cm.android.framework.client.core.LogUtil;
import cm.android.framework.interfaces.IServiceFetcher;
import cm.android.framework.server.ServerProvider;
import cm.android.framework.server.ServiceCache;

@Deprecated
public class ServiceManagerNative {

    private static IServiceFetcher sFetcher;

    public synchronized static IServiceFetcher getServiceFetcher() {
        if (sFetcher == null) {
            Context context = Framework.get().getBaseContext();
            Bundle response = ServerProvider.Proxy.getServiceFetcher(context);
            if (response != null) {
                IBinder binder = BundleCompat.getBinder(response, ServerProvider.KEY_BINDER);
                LocalProxyUtils.linkBinderDied(binder, new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        //TODO ggg server挂掉会重启，无需kill。但是需要重新清下数据
                        sFetcher = null;
                    }
                });
                sFetcher = IServiceFetcher.Stub.asInterface(binder);
            }
        }
        return sFetcher;
    }

    public static IBinder getService(String name) {
        if (Framework.get().isServerProcess()) {
            return ServiceCache.getService(name);
        }
        IServiceFetcher fetcher = getServiceFetcher();
        if (fetcher != null) {
            try {
                return fetcher.getService(name);
            } catch (RemoteException e) {
                LogUtil.getLogger().error(e.getMessage(), e);
            }
        }
        LogUtil.getLogger().error("GetService(%s) return null.", name);
        return null;
    }

    public static void addService(String name, IBinder service) {
        if (Framework.get().isServerProcess()) {
            ServiceCache.addService(name, service);
            return;
        }

        IServiceFetcher fetcher = getServiceFetcher();
        if (fetcher != null) {
            try {
                fetcher.addService(name, service);
            } catch (RemoteException e) {
                LogUtil.getLogger().error(e.getMessage(), e);
            }
        }

    }

    public static void removeService(String name) {
        if (Framework.get().isServerProcess()) {
            ServiceCache.removeService(name);
            return;
        }

        IServiceFetcher fetcher = getServiceFetcher();
        if (fetcher != null) {
            try {
                fetcher.removeService(name);
            } catch (RemoteException e) {
                LogUtil.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public static void clearService() {
        if (Framework.get().isServerProcess()) {
            ServiceCache.clearService();
            return;
        }

        IServiceFetcher fetcher = getServiceFetcher();
        if (fetcher != null) {
            try {
                fetcher.clearService();
            } catch (RemoteException e) {
                LogUtil.getLogger().error(e.getMessage(), e);
            }
        }
    }

}
