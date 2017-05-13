package cm.android.framework.client.ipc;

import android.os.IBinder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cm.android.framework.client.core.LogUtil;
import cm.java.proguard.annotations.Keep;

public final class BinderFactory {

    public static interface IBinderProxy {

        @Keep
        void bind(IBinder binder);

        void binderDied();
    }

//    public static HashMap<String, Class> proxy = ObjectUtil.newHashMap();

    public static <T> T getProxy(String name, Class<? extends IBinderProxy> proxyClass) {
//        Class proxyClass = proxy.get(name);
//        if (proxyClass == null) {
//            return null;
//        }

        try {
            Constructor constructor = proxyClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            final IBinderProxy proxy = (IBinderProxy) constructor.newInstance();

//            IBinder binder = ServiceManagerNative.getService(name);
            IBinder binder = ServiceManager.getService(name);
            LocalProxyUtils.linkBinderDied(binder, new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    proxy.binderDied();
                }
            });
            proxy.bind(binder);
            return (T) proxy;
        } catch (NoSuchMethodException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
            return null;
        } catch (InstantiationException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
            return null;
        } catch (IllegalAccessException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
            return null;
        } catch (InvocationTargetException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
            return null;
        }
    }
}
