package cm.android.app.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;
import android.os.RemoteException;

import cm.android.framework.core.BinderFactory;
import cm.android.framework.ext.alarm.ITimerTaskServer;

public class TimerTaskManager extends ITimerTaskServer.Stub implements BinderFactory.IBinderProxy {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    private ITimerTaskServer timerTaskManager;

    @Override
    public void register(String action, long period, boolean globalBroadcast) {
        try {
            timerTaskManager.register(action, period, globalBroadcast);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void unregister(String action) {
        try {
            timerTaskManager.unregister(action);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void bind(IBinder binder) {
        timerTaskManager = ITimerTaskServer.Stub.asInterface(binder);
    }
}
