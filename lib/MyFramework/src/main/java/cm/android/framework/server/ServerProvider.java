package cm.android.framework.server;

import android.content.Context;
import android.content.pm.ProviderInfo;
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
    public static final String M_getBundle = "@getBundle";
    public static final String M_putBundle = "@putBundle";

    public static String AUTHORITIES = "framework.provider";

    public static final String KEY_BINDER = "_framework_|_binder_";

    private final ServiceFetcher mServiceFetcher = new ServiceFetcher();
    private final BinderServerAgent binderServer = new BinderServerAgent();

    public static void authorities(String authorities) {
        AUTHORITIES = authorities;
    }

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
    }

    @Override
    public boolean onCreate() {
        LogUtil.getLogger().info("ServerProvider:onCreate:{},getContext():{}", this, getContext());
        DaemonService.start(this, getContext());

        synchronized (this) {
            binderServer.attach(Framework.SERVER_NAME);
            //TODO ggg 方案1：create时判断是否初始化
            //TODO ggg 确保getContext().getApplicationContext()不为null
            binderServer.restore(getContext());
        }
        return true;
    }

//    private void addService(String name, IBinder service) {
//        ServiceFetcherServer.get().addService(name, service);
//    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        LogUtil.getLogger().info("ServerProvider:isStartup = {},method = {},arg = {},extras = {}", Framework.get().isStartup(), method, arg, extras);

        synchronized (this) {
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
            } else if (M_getBundle.equals(method)) {
                Bundle bundle = new Bundle();
                bundle.putBundle(KEY_BINDER, binderServer.getBundle(arg));
                return bundle;
            } else if (M_putBundle.equals(method)) {
                binderServer.putBundle(arg, extras);
            }
        }
        return null;
    }

    private static class ServiceFetcher extends IServiceFetcher.Stub {
        @Override
        public IBinder getService(String name) throws RemoteException {
            if (name != null) {
                return ServiceFetcherServer.get().getService(name);
            }
            return null;
        }

        @Override
        public void addService(String name, IBinder service) throws RemoteException {
            if (name != null && service != null) {
                ServiceFetcherServer.get().addService(name, service);
            }
        }

        @Override
        public void removeService(String name) throws RemoteException {
            if (name != null) {
                ServiceFetcherServer.get().removeService(name);
            }
        }

        @Override
        public void clearService() throws RemoteException {
            ServiceFetcherServer.get().clearService();
        }
    }

    public static class Proxy {
        private static Bundle invokeMethod(Context context, String method) {
            Bundle response = new ProviderCall.Builder(context, AUTHORITIES)
                    .methodName(method)
                    .call();
            return response;
        }

        private static Bundle invokeMethod(Context context, String method, String arg, Bundle bundle) {
            Bundle response = new ProviderCall.Builder(context, AUTHORITIES)
                    .methodName(method)
                    .arg(arg)
                    .addArg(bundle)
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

        public static Bundle getBundle(Context context, String key) {
            return invokeMethod(context, ServerProvider.M_getBundle, key, null);
        }

        public static Bundle putBundle(Context context, String key, Bundle bundle) {
            return invokeMethod(context, ServerProvider.M_putBundle, key, bundle);
        }
    }
}
