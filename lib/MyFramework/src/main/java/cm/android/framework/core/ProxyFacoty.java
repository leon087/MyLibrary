package cm.android.framework.core;

import java.lang.reflect.Constructor;

public final class ProxyFacoty {

    private static Class<? extends IBaseProxy> proxyClass;

    private static Object proxy;

    public static void register(Class<? extends IBaseProxy> proxyClass) {
        ProxyFacoty.proxyClass = proxyClass;
    }

    public static <T> T create() {
        try {
            Constructor constructor = proxyClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            T proxy = (T) constructor.newInstance();
            return proxy;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getProxy() {
        if (proxy == null) {
            proxy = create();
        }
        return (T) proxy;
    }

    public static interface IBaseProxy {

        String getName();
    }
}
