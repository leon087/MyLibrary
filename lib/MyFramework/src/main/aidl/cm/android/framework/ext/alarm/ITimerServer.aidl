package cm.android.framework.ext.alarm;

interface ITimerServer {
    void register(String action, long period, boolean globalBroadcast);

    void unregister(String action);
}
