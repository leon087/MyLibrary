package cm.android.framework.ext.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;
import android.os.RemoteException;

import cm.android.framework.core.BinderFactory;

public final class TimerManager extends ITimerServer.Stub implements BinderFactory.IBinderProxy {

    private static final Logger logger = LoggerFactory.getLogger("timer");

    private ITimerServer timerManager;

    @Override
    public void register(String action, long period, boolean globalBroadcast) {
        try {
            timerManager.register(action, period, globalBroadcast);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void unregister(String action) {
        try {
            timerManager.unregister(action);
        } catch (RemoteException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void bind(IBinder binder) {
        timerManager = ITimerServer.Stub.asInterface(binder);
    }
}
