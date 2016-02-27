package cm.android.app.flux.action;

import cm.android.app.flux.dispatcher.Dispatcher;

public class TestActionCreator implements ActionCreator<TestAction> {

    private static TestActionCreator instance;
    final Dispatcher dispatcher;

    TestActionCreator(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static TestActionCreator get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new TestActionCreator(dispatcher);
        }
        return instance;
    }

    public void send(String text) {
        Action action = createAction(TestAction.ACTION_SEND, text);
        dispatcher.dispatch(action);
    }

    @Override
    public TestAction createAction(String type, Object... params) {
        String text = (String) params[0];
        TestAction action = new TestAction(type, text);
        return action;
    }
}
