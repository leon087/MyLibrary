package cm.android.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.text.TextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 *         <p>
 *         HookHandler uses Java's {@link Proxy} to create a wrapper for existing services.
 *         <p>
 *         When any method is called on the wrapper, it checks if there is any {@link MethodProxy} registered
 *         and enabled for that method. If so, it calls the startUniformer instead of the wrapped implementation.
 *         <p>
 *         The whole thing is managed by a {@link MethodInvocationProxy} subclass
 */
@SuppressWarnings("unchecked")
public class MethodInvocationStub<T> {

    private static Logger logger = LoggerFactory.getLogger("MethodInvocationStub");

    private Map<String, MethodProxy> mInternalMethodProxies = new HashMap<>();
    private T mBaseInterface;
    private T mProxyInterface;
    private String mIdentityName;


    public Map<String, MethodProxy> getAllHooks() {
        return mInternalMethodProxies;
    }


    public MethodInvocationStub(T baseInterface, Class<?>... proxyInterfaces) {
        this.mBaseInterface = baseInterface;
        if (baseInterface != null) {
            if (proxyInterfaces == null) {
                proxyInterfaces = MethodParameterUtils.getAllInterface(baseInterface.getClass());
            }
            mProxyInterface = (T) Proxy.newProxyInstance(baseInterface.getClass().getClassLoader(), proxyInterfaces, new HookInvocationHandler());
        } else {
            logger.debug("Unable to build HookDelegate: {}.", getIdentityName());
        }
    }

    public void setIdentityName(String identityName) {
        this.mIdentityName = identityName;
    }

    public String getIdentityName() {
        if (mIdentityName != null) {
            return mIdentityName;
        }
        return getClass().getSimpleName();
    }

    public MethodInvocationStub(T baseInterface) {
        this(baseInterface, (Class[]) null);
    }

    /**
     * Copy all proxies from the input HookDelegate.
     *
     * @param from the HookDelegate we copy from.
     */
    public void copyMethodProxies(MethodInvocationStub from) {
        this.mInternalMethodProxies.putAll(from.getAllHooks());
    }

    /**
     * Add a method proxy.
     *
     * @param methodProxy proxy
     */
    public MethodProxy addMethodProxy(MethodProxy methodProxy) {
        if (methodProxy != null && !TextUtils.isEmpty(methodProxy.getMethodName())) {
            if (mInternalMethodProxies.containsKey(methodProxy.getMethodName())) {
                logger.warn("The Hook({}, {}) you added has been in existence.", methodProxy.getMethodName(),
                        methodProxy.getClass().getName());
                return methodProxy;
            }
            mInternalMethodProxies.put(methodProxy.getMethodName(), methodProxy);
        }
        return methodProxy;
    }

    /**
     * Remove a method proxy.
     *
     * @param hookName proxy
     * @return The proxy you removed
     */
    public MethodProxy removeMethodProxy(String hookName) {
        return mInternalMethodProxies.remove(hookName);
    }

    /**
     * Remove a method proxy.
     *
     * @param methodProxy target proxy
     */
    public void removeMethodProxy(MethodProxy methodProxy) {
        if (methodProxy != null) {
            removeMethodProxy(methodProxy.getMethodName());
        }
    }

    /**
     * Remove all method proxies.
     */
    public void removeAllMethodProxies() {
        mInternalMethodProxies.clear();
    }

    /**
     * Get the startUniformer by its name.
     *
     * @param name name of the Hook
     * @param <H>  Type of the Hook
     * @return target startUniformer
     */
    @SuppressWarnings("unchecked")
    public <H extends MethodProxy> H getMethodProxy(String name) {
        return (H) mInternalMethodProxies.get(name);
    }

    /**
     * @return Proxy interface
     */
    public T getProxyInterface() {
        return mProxyInterface;
    }

    /**
     * @return Origin Interface
     */
    public T getBaseInterface() {
        return mBaseInterface;
    }

    /**
     * @return count of the hooks
     */
    public int getMethodProxiesCount() {
        return mInternalMethodProxies.size();
    }

    private class HookInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodProxy methodProxy = getMethodProxy(method.getName());
            try {
                if (methodProxy != null && methodProxy.isEnable()) {
                    if (methodProxy.beforeCall(mBaseInterface, method, args)) {
                        Object res = methodProxy.call(mBaseInterface, method, args);
                        res = methodProxy.afterCall(mBaseInterface, method, args, res);
                        return res;
                    }
                }
                return method.invoke(mBaseInterface, args);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getTargetException();
                if (cause != null) {
                    throw cause;
                }
                throw e;
            }
        }
    }

}
