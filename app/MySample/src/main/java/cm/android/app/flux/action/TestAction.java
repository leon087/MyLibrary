package cm.android.app.flux.action;

public class TestAction extends Action<String> {
    public static final String ACTION_SEND = "test_send";

    TestAction(String type, String data) {
        super(type, data);
    }
}
