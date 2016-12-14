package cm.android.framework.ext.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.IBinder;

import cm.android.framework.client.ipc.BinderFactory;
import cm.android.framework.client.ipc.LocalProxyUtils;

public final class TimerManager extends ITimerServer.Stub implements BinderFactory.IBinderProxy {

    private static final Logger logger = LoggerFactory.getLogger("timer");

    private ITimerServer timerServer;

    @Override
    public synchronized void register(String action, long period, long delay) {
        if (timerServer == null) {
            logger.error("register:timerServer = null");
            return;
        }
        try {
            ITimerServer server = LocalProxyUtils.genProxy(ITimerServer.class, timerServer);
            server.register(action, period, delay);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public synchronized void unregister(String action) {
        if (timerServer == null) {
            logger.error("unregister:timerServer = null");
            return;
        }
        try {
            ITimerServer server = LocalProxyUtils.genProxy(ITimerServer.class, timerServer);
            server.unregister(action);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public synchronized void bind(IBinder binder) {
        timerServer = ITimerServer.Stub.asInterface(binder);
    }

    @Override
    public synchronized void binderDied() {
        timerServer = null;
    }
}
