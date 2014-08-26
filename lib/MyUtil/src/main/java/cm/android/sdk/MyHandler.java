package cm.android.sdk;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public abstract class MyHandler {

    private final WeakHandler handler = new WeakHandler(this);

    public abstract void handleMessage(Message msg);

    public Handler getHandler() {
        return handler;
    }

    public void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public Message obtainMessage(int what) {
        return handler.obtainMessage(what);
    }

    public Message obtainMessage(int what, Object obj) {
        return handler.obtainMessage(what, obj);
    }

    public void sendEmptyMessage(int what) {
        handler.sendEmptyMessage(what);
    }

    private static class WeakHandler extends Handler {
        private WeakReference<MyHandler> mOuter;

        public WeakHandler(MyHandler callback) {
            mOuter = new WeakReference<MyHandler>(callback);
        }

        @Override
        public final void handleMessage(Message msg) {
            MyHandler outer = mOuter.get();
            if (outer != null) {
                outer.handleMessage(msg);
            }
        }
    }

    // void xx(){
    // handler.sendEmptyMessage(what);
    // handler.sendEmptyMessageAtTime(what, uptimeMillis)
    // handler.sendEmptyMessageDelayed(what, delayMillis)
    // }
}
