package cm.android.app.test.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;
import android.os.RemoteException;

import cm.android.app.sample.ITestManager;
import cm.android.framework.client.ipc.BinderFactory;
import cm.android.framework.client.ipc.LocalProxyUtils;

public class TestManager extends ITestManager.Stub implements BinderFactory.IBinderProxy {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private ITestManager testManager;

    @Override
    public void count() {
        try {
            if (testManager == null || !testManager.asBinder().isBinderAlive()) {
                android.util.Log.e("ggg", "ggg testManager = null");
                return;
            }

            ITestManager server = LocalProxyUtils.genProxy(ITestManager.class, testManager);
            server.count();
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void bind(IBinder binder) {
        android.util.Log.e("ggg", "ggg binder = " + binder);
        testManager = ITestManager.Stub.asInterface(binder);
    }

    @Override
    public void binderDied() {
        testManager = null;
    }
}
