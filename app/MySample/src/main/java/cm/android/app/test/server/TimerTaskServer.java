package cm.android.app.test.server;

import android.os.RemoteException;

import cm.android.app.core.MainApp;
import cm.android.framework.ext.alarm.ITimerTaskServer;

public class TimerTaskServer extends ITimerTaskServer.Stub {

    private cm.android.sdk.alarm.TimerTaskManager timerTaskManager
            = new cm.android.sdk.alarm.TimerTaskManager();

    public void start() {
        timerTaskManager.start(MainApp.getApp());
    }

    public void stop() {
        timerTaskManager.stop();
    }

    @Override
    public void register(String action, long period, boolean globalBroadcast)
            throws RemoteException {
        timerTaskManager.register(action, period, globalBroadcast);
    }

    @Override
    public void unregister(String action) throws RemoteException {
        timerTaskManager.unregister(action);
    }
}