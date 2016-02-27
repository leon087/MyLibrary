package cm.android.app.flux.action;

public class Action<T> {
    private final String type;
    private final T data;

    Action(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public T getData() {
        return data;
    }
}
