package cm.android.framework.ext.alarm;

interface ITimerServer {
    void register(String action, long period);

    void unregister(String action);
}
