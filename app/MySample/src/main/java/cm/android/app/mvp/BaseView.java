package cm.android.app.mvp;

public interface BaseView<T> {
    void init(T presenter);
}