package cm.java.util;

public abstract class Singleton<T> {
    private volatile T instance;

    protected abstract T create();

    public final T get() {
        if (instance == null) {
            synchronized (this) {
                if (instance == null) {
                    instance = create();
                }
            }
        }
        return instance;
    }
}
