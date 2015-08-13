package cm.android.framework.ext.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;

import cm.android.framework.core.BinderFactory;

public final class TimerManager extends ITimerServer.Stub implements BinderFactory.IBinderProxy {

    private static final Logger logger = LoggerFactory.getLogger("timer");

    private ITimerServer timerManager;

    @Override
    public void register(String action, long period) {
        try {
            timerManager.register(action, period);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void unregister(String action) {
        try {
            timerManager.unregister(action);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void bind(IBinder binder) {
        timerManager = ITimerServer.Stub.asInterface(binder);
    }
}
