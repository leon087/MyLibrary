package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cm.android.proguard.annotations.Keep;

public final class BinderFactory {

    public static interface IBinderProxy {

        @Keep
        void bind(IBinder binder);
    }

    private static final Logger logger = LoggerFactory.getLogger("framework");

//    public static HashMap<String, Class> proxy = ObjectUtil.newHashMap();

    public static <T> T getProxy(String name, Class<? extends IBinderProxy> proxyClass) {
//        Class proxyClass = proxy.get(name);
//        if (proxyClass == null) {
//            return null;
//        }

        try {
            Constructor constructor = proxyClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            IBinderProxy proxy = (IBinderProxy) constructor.newInstance();

            IBinder binder = ServiceManager.getService(name);
            proxy.bind(binder);
            return (T) proxy;
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
            return null;
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
            return null;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
