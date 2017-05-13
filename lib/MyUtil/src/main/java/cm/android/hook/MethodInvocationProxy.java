package cm.android.hook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 *         <p>
 *         This class is responsible with:
 *         - Instantiating a {@link MethodInvocationStub.HookInvocationHandler} on {@link
 *         #getInvocationStub()} ()}
 *         - Install a bunch of {@link MethodProxy}s, either with a @{@link Inject} annotation or
 *         manually
 *         calling {@link #addMethodProxy(MethodProxy)} from {@link #onBindMethods()}
 *         - Install the hooked object on the Runtime via {@link #inject()}
 *         <p>
 *         All {@link MethodInvocationProxy}s (plus a couple of other @{@link IInjector}s are
 *         installed by
 */
public abstract class MethodInvocationProxy<T extends MethodInvocationStub> implements IInjector {

    protected T mInvocationStub;

    public MethodInvocationProxy(T invocationStub) {
        this.mInvocationStub = invocationStub;
        onBindMethods();
        afterHookApply(invocationStub);
    }

    protected void onBindMethods() {

        if (mInvocationStub == null) {
            return;
        }
        Class<? extends MethodInvocationProxy> clazz = getClass();
        Inject inject = clazz.getAnnotation(Inject.class);
        if (inject != null) {
            Class<?> proxiesClass = inject.value();
            Class<?>[] innerClasses = proxiesClass.getDeclaredClasses();
            for (Class<?> innerClass : innerClasses) {
                if (!Modifier.isAbstract(innerClass.getModifiers())
                        && MethodProxy.class.isAssignableFrom(innerClass)
                        && innerClass.getAnnotation(SkipInject.class) == null) {
                    addMethodProxy(innerClass);
                }
            }

        }
    }

    private void addMethodProxy(Class<?> hookType) {
        try {
            Constructor<?> constructor = hookType.getDeclaredConstructors()[0];
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            MethodProxy methodProxy;
            if (constructor.getParameterTypes().length == 0) {
                methodProxy = (MethodProxy) constructor.newInstance();
            } else {
                methodProxy = (MethodProxy) constructor.newInstance(this);
            }
            mInvocationStub.addMethodProxy(methodProxy);
        } catch (Throwable e) {
            throw new RuntimeException("Unable to instance Hook : " + hookType + " : " + e.getMessage());
        }
    }

    public MethodProxy addMethodProxy(MethodProxy methodProxy) {
        return mInvocationStub.addMethodProxy(methodProxy);
    }

    protected void afterHookApply(T delegate) {
    }

    @Override
    public abstract void inject() throws Throwable;

    public T getInvocationStub() {
        return mInvocationStub;
    }
}
