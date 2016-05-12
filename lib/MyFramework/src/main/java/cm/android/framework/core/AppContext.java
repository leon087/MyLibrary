package cm.android.framework.core;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class AppContext {
    private static class ServiceFetcher {
        public Object service = null;

        public Object getService() {
            if (service != null) {
                return service;
            }
            service = createService();
            return service;
        }

        public Object createService() {
            throw new RuntimeException("Notimplemented");
        }
    }

    private static final Map<String, WeakReference<ServiceFetcher>> SERVICE_MAP = new HashMap<>();

    public static void register(final String name, final Class<? extends BinderFactory.IBinderProxy> proxyClass) {
        SERVICE_MAP.put(name, new WeakReference<ServiceFetcher>(new ServiceFetcher() {
            @Override
            public Object createService() {
                return BinderFactory.getProxy(name, proxyClass);
            }
        }));
    }

    public static Object getService(String name) {
        WeakReference<ServiceFetcher> reference = SERVICE_MAP.get(name);
        if (reference == null) {
            return null;
        }
        ServiceFetcher fetcher = reference.get();
        return fetcher == null ? null : fetcher.getService();
    }

    public static <T> T getService(String name, Class<? extends BinderFactory.IBinderProxy> proxyClass) {
        Object obj = getService(name);
        if (obj != null) {
            return (T) obj;
        }

        register(name, proxyClass);
        return (T) getService(name);
    }
}
