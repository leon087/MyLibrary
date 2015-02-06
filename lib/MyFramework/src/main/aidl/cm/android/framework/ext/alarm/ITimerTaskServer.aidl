package cm.android.framework.ext.alarm;

interface ITimerTaskServer {

    void register(String action, long period, boolean globalBroadcast);

    void unregister(String action);
}
