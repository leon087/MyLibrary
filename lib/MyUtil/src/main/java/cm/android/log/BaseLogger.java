package cm.android.log;

/**
 */
public abstract class BaseLogger {

    protected BaseLogger(Class clazz) {
        init(clazz);
    }

    protected abstract void init(Class<?> clazz);

    protected abstract void deInit();
}


