package cm.android.framework.ext.alarm;

import android.content.Context;
import android.os.RemoteException;

public final class TimerServer extends ITimerServer.Stub {

    private cm.android.sdk.alarm.TimerTaskManager timerTaskManager
            = new cm.android.sdk.alarm.TimerTaskManager();

    public TimerServer() {
    }

    public void start(Context context) {
        timerTaskManager.start(context);
    }

    public void stop() {
        timerTaskManager.stop();
    }

    @Override
    public void register(String action, long period, long delay) throws RemoteException {
        timerTaskManager.register(action, period, delay);
    }

    @Override
    public void unregister(String action) throws RemoteException {
        timerTaskManager.unregister(action);
    }
}
