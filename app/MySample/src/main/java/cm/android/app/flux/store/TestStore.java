package cm.android.app.flux.store;

import cm.android.app.flux.action.TestAction;
import cm.android.app.flux.model.Test;

public class TestStore extends Store<TestAction> {
    private final Test model = new Test();

    public Test getModel() {
        return model;
    }

    @Override
    public StoreChangeEvent changeEvent() {
        return new StoreChangeEvent();
    }

    @Override
    public void onAction(TestAction action) {
        switch (action.getType()) {
            case TestAction.ACTION_SEND:
                model.setText(action.getData());
                break;
            default:
        }
        emitStoreChange();
    }
}
