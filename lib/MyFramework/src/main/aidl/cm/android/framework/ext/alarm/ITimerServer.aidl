package cm.android.framework.ext.alarm;

interface ITimerServer {
    void register(String action, long period, long delay);

    void unregister(String action);
}
