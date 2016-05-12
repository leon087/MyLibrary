package cm.android.sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public abstract class WeakHandler {

    private Handler handler = null;

    public WeakHandler() {
        handler = new InnerHandler(this);
    }

    public WeakHandler(Looper looper) {
        handler = new InnerHandler(this, looper);
    }

    public abstract void handleMessage(Message msg);

    public Handler get() {
        return handler;
    }

    private static class InnerHandler extends Handler {

        private WeakReference<cm.android.sdk.WeakHandler> mOuter;

        public InnerHandler(cm.android.sdk.WeakHandler callback) {
            super();
            mOuter = new WeakReference<>(callback);
        }

        public InnerHandler(cm.android.sdk.WeakHandler callback, Looper looper) {
            super(looper);
            mOuter = new WeakReference<>(callback);
        }

        @Override
        public final void handleMessage(Message msg) {
            cm.android.sdk.WeakHandler outer = mOuter.get();
            if (outer != null) {
                outer.handleMessage(msg);
            }
        }
    }
}
