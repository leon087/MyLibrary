package cm.android.app.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;
import android.os.RemoteException;

import cm.android.app.sample.ITestManager;
import cm.android.framework.core.ProxyFactory;

public class TestManager extends ITestManager.Stub {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private int count;

    @Override
    public void count() throws RemoteException {
        logger.error("ggg count = " + count);
        count++;
    }

    public static class TestManagerProxy extends ITestManager.Stub implements
            ProxyFactory.IBinderProxy {

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
}
