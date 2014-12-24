package cm.android.log;

/**
 */
public interface ILogger {

    void d(String msg);

    void i(String msg);

    void e(String msg);

    void e(String msg, Throwable e);

    void e(Throwable e);
}
