package cm.android.app.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.RemoteException;

import cm.android.app.sample.ITestManager;

public class TestManagerServer extends ITestManager.Stub {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private int count;

    @Override
    public void count() throws RemoteException {
        logger.error("ggg count = " + count);
        count++;
    }
}
