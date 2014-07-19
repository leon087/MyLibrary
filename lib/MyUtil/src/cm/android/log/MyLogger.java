package cm.android.log;

import cm.android.util.MyLog;

/**
 */
public class MyLogger extends BaseLogger implements ILogger {

    protected MyLogger(Class clazz) {
        super(clazz);
    }

    public static ILogger getLogger(Class clazz) {
        MyLogger logger = new MyLogger(clazz);
        return logger;
    }

    @Override
    protected void init(Class<?> clazz) {
    }

    @Override
    protected void deInit() {
    }

    @Override
    public void d(String msg) {
        MyLog.e(msg);
    }

    @Override
    public void i(String msg) {
        MyLog.i(msg);
    }

    @Override
    public void e(String msg) {
        MyLog.e(msg);
    }

    @Override
    public void e(String msg, Throwable e) {
        MyLog.e(msg, e);
    }

    @Override
    public void e(Throwable e) {
        MyLog.e(e);
    }
}

class Test {
    private ILogger logger = MyLogger.getLogger(this.getClass());

    private void test() {
        logger.e("test");
    }

}
