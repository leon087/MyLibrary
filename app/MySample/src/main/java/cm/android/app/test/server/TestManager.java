package cm.android.app.test.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;
import android.os.RemoteException;

import cm.android.app.sample.ITestManager;
import cm.android.framework.core.BinderFactory;

public class TestManager extends ITestManager.Stub implements BinderFactory.IBinderProxy {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private ITestManager testManager;

    @Override
    public void count() {
        try {
            testManager.count();
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void bind(IBinder binder) {
        testManager = ITestManager.Stub.asInterface(binder);
    }
}
