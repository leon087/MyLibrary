package cm.android.framework.client.ipc;

import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cm.android.framework.client.core.LogUtil;

public class LocalProxyUtils {

    /**
     * Generates the Proxy instance for a base object, each IPC call will clean its calling
     * identity.
     *
     * @param interfaceClass interface class
     * @param base           base object
     * @return proxy object
     */
    public static <T> T genProxy(Class<T> interfaceClass, final Object base) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                long identity = Binder.clearCallingIdentity();
                try {
                    return method.invoke(base, args);
                } finally {
                    Binder.restoreCallingIdentity(identity);
                }
            }
        });
    }

    public static void linkBinderDied(final IBinder binder, final IBinder.DeathRecipient recipient) {
        if (binder == null) {
            LogUtil.getLogger().error("linkBinderDied:binder = null");
            return;
        }
        IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                binder.unlinkToDeath(this, 0);
                LogUtil.getLogger().error("the server has crashed:binder = {}", binder);
                recipient.binderDied();
            }
        };
        try {
            binder.linkToDeath(deathRecipient, 0);
        } catch (RemoteException e) {
            LogUtil.getLogger().error(e.getMessage(), e);
        }
    }
}
