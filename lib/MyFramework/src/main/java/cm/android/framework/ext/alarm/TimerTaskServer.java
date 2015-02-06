package cm.android.framework.ext.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import cm.android.framework.core.IServer;
import cm.android.sdk.alarm.TimerTaskManager;

public final class TimerTaskServer implements IServer {

    private TimerTaskManager timerTaskManager = new TimerTaskManager();

    public static final String ACTION_TIMER_TASK = "cm.android.framework.alarm.TimerTaskServer";

    @Override
    public void onCreate(Context context) {
        timerTaskManager.start(context);
    }

    @Override
    public void onDestroy() {
        timerTaskManager.stop();
    }

    @Override
    public boolean checkAction(String action) {
        if (ACTION_TIMER_TASK.equals(action)) {
            return true;
        }
        return false;
    }

    @Override
    public void onHandleIntent(Intent intent) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return timerTaskServer;
    }

    private IBinder timerTaskServer = new ITimerTaskServer.Stub() {

        @Override
        public void register(String action, long period, boolean globalBroadcast)
                throws RemoteException {
            timerTaskManager.register(action, period, globalBroadcast);
        }

        @Override
        public void unregister(String action) throws RemoteException {
            timerTaskManager.unregister(action);
        }
    };
}
