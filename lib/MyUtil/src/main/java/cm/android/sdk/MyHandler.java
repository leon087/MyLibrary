package cm.android.sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

@Deprecated
public abstract class MyHandler {

    private WeakHandler handler = null;

    public MyHandler() {
        handler = new WeakHandler(this);
    }

    public MyHandler(Looper looper) {
        handler = new WeakHandler(this, looper);
    }

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
            super();
            mOuter = new WeakReference<MyHandler>(callback);
        }

        public WeakHandler(MyHandler callback, Looper looper) {
            super(looper);
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
