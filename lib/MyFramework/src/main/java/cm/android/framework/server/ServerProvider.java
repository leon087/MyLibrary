package cm.android.framework.server;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.BundleCompat;

import cm.android.framework.client.core.Framework;
import cm.android.framework.client.core.LogUtil;
import cm.android.framework.client.ipc.ProviderCall;
import cm.android.framework.component.BaseContentProvider;
import cm.android.framework.interfaces.IServiceFetcher;
import cm.android.framework.server.daemon.DaemonService;

public final class ServerProvider extends BaseContentProvider {
    public static final String M_create = "@create";
    public static final String M_destroy = "@destroy";
    public static final String M_isActive = "@isActive";
    public static final String M_getServiceFetcher = "@getServiceFetcher";

    public static String SERVICE_AUTH = "framework.ServerProvider";

    public static final String KEY_BINDER = "_framework_|_binder_";

    private final ServiceFetcher mServiceFetcher = new ServiceFetcher();
    private final BinderServerAgent binderServer = new BinderServerAgent();

    public static void authoritiy(String authoritiy) {
        SERVICE_AUTH = authoritiy;
    }

    @Override
    public boolean onCreate() {
        LogUtil.getLogger().info("ServerProvider:onCreate:{},getContext():{}", this, getContext());
        DaemonService.start(getContext());

        binderServer.attach(Framework.SERVER_NAME);
        //TODO ggg 方案1：create时判断是否初始化
        //TODO ggg 确保getContext().getApplicationContext()不为null
        binderServer.restore(getContext());

        return true;
    }

    private void addService(String name, IBinder service) {
        ServiceCache.addService(name, service);
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        LogUtil.getLogger().info("ServerProvider:isStartup = {},method = {},arg = {},extras = {}", Framework.get().isStartup(), method, arg, extras);

        if (M_create.equals(method)) {
            binderServer.create(getContext());
        } else if (M_destroy.equals(method)) {
            binderServer.destroy();
        } else if (M_isActive.equals(method)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(KEY_BINDER, binderServer.isActive(getContext()));
            return bundle;
        } else if (M_getServiceFetcher.equals(method)) {
            //TODO 方案2:ggg 每次get前判断下是否初始化
//            binderServer.restore(getContext());

            Bundle bundle = new Bundle();
            BundleCompat.putBinder(bundle, KEY_BINDER, mServiceFetcher);
            return bundle;
        }

        return null;
    }

    private class ServiceFetcher extends IServiceFetcher.Stub {
        @Override
        public IBinder getService(String name) throws RemoteException {
            if (name != null) {
                return ServiceCache.getService(name);
            }
            return null;
        }

        @Override
        public void addService(String name, IBinder service) throws RemoteException {
            if (name != null && service != null) {
                ServiceCache.addService(name, service);
            }
        }

        @Override
        public void removeService(String name) throws RemoteException {
            if (name != null) {
                ServiceCache.removeService(name);
            }
        }

        @Override
        public void clearService() throws RemoteException {
            ServiceCache.clearService();
        }
    }

    public static class Proxy {
        private static Bundle invokeMethod(Context context, String method) {
            Bundle response = new ProviderCall.Builder(context, SERVICE_AUTH)
                    .methodName(method)
                    .call();
            return response;
        }

        public static void create(Context context) {
            invokeMethod(context, ServerProvider.M_create);
        }

        public static void destroy(Context context) {
            invokeMethod(context, ServerProvider.M_destroy);
        }

        public static Bundle isActive(Context context) {
            return invokeMethod(context, ServerProvider.M_isActive);
        }

        public static Bundle getServiceFetcher(Context context) {
            return invokeMethod(context, ServerProvider.M_getServiceFetcher);
        }
    }
}
