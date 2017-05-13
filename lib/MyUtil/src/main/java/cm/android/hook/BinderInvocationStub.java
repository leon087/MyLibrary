package cm.android.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.FileDescriptor;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class BinderInvocationStub extends MethodInvocationStub<IInterface> implements IBinder {

    private static Logger logger = LoggerFactory.getLogger("BinderInvocationStub");

    private static final String TAG = BinderInvocationStub.class.getSimpleName();
    private IBinder mBaseBinder;

    public BinderInvocationStub(Class<?> stubClass, IBinder binder) {
        this(asInterface(stubClass, binder));
    }


    public BinderInvocationStub(IInterface mBaseInterface) {
        super(mBaseInterface);
        mBaseBinder = getBaseInterface() != null ? getBaseInterface().asBinder() : null;
        addMethodProxy(new AsBinder());
    }

    private static IInterface asInterface(Class<?> stubClass, IBinder binder) {
        try {
            if (stubClass == null || binder == null) {
                return null;
            }
            Method asInterface = stubClass.getMethod("asInterface", IBinder.class);
            return (IInterface) asInterface.invoke(null, binder);
        } catch (Exception e) {
            logger.error("Could not create stub {}, cause {}", stubClass.getName(), e);
            return null;
        }
    }

    public void replaceService(String name) {
        if (mBaseBinder != null) {
            HookHelper.getsCache().put(name, this);
        }
    }

    private final class AsBinder extends MethodProxy {

        @Override
        public String getMethodName() {
            return "asBinder";
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            return BinderInvocationStub.this;
        }
    }


    @Override
    public String getInterfaceDescriptor() throws RemoteException {
        return mBaseBinder.getInterfaceDescriptor();
    }

    @Override
    public boolean pingBinder() {
        return mBaseBinder.pingBinder();
    }

    @Override
    public boolean isBinderAlive() {
        return mBaseBinder.isBinderAlive();
    }

    @Override
    public IInterface queryLocalInterface(String descriptor) {
        return getProxyInterface();
    }

    @Override
    public void dump(FileDescriptor fd, String[] args) throws RemoteException {
        mBaseBinder.dump(fd, args);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {
        mBaseBinder.dumpAsync(fd, args);
    }

    @Override
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return mBaseBinder.transact(code, data, reply, flags);
    }

    @Override
    public void linkToDeath(DeathRecipient recipient, int flags) throws RemoteException {
        mBaseBinder.linkToDeath(recipient, flags);
    }

    @Override
    public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
        return mBaseBinder.unlinkToDeath(recipient, flags);
    }

    public IBinder getBaseBinder() {
        return mBaseBinder;
    }

}
