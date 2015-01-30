package cm.android.framework.core;

import java.lang.reflect.Constructor;

public final class ProxyFacoty {

    private static Class<? extends IBaseProxy> proxyClass;

    public static void register(Class<? extends IBaseProxy> proxyClass) {
        ProxyFacoty.proxyClass = proxyClass;
    }

    public static <T> T getProxy() {
        try {
            Constructor constructor = proxyClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            T proxy = (T) constructor.newInstance();
            return proxy;
        } catch (Exception e) {
            return null;
        }
    }

    public static interface IBaseProxy {

        String getName();
    }
}
