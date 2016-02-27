package cm.android.app.flux.dispatcher;

import java.util.ArrayList;
import java.util.List;

import cm.android.app.flux.action.Action;
import cm.android.app.flux.store.Store;

public class Dispatcher {
    private static Dispatcher instance;
    private final List<Store> stores = new ArrayList<>();

    public static Dispatcher get() {
        if (instance == null) {
            synchronized (Dispatcher.class) {
                if (instance == null) {
                    instance = new Dispatcher();
                }
            }
        }
        return instance;
    }

    private Dispatcher() {
    }

    public void register(final Store store) {
        if (!stores.contains(store)) {
            stores.add(store);
        }
    }

    public void unregister(final Store store) {
        stores.remove(store);
    }

    public void dispatch(Action action) {
        post(action);
    }

    private void post(final Action action) {
        for (Store store : stores) {
            store.onAction(action);
        }
    }
}
