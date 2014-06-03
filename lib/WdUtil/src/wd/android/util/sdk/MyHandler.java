package wd.android.util.sdk;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public abstract class MyHandler {

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

	public void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}

	// void xx(){
	// handler.sendEmptyMessage(what);
	// handler.sendEmptyMessageAtTime(what, uptimeMillis)
	// handler.sendEmptyMessageDelayed(what, delayMillis)
	// }
}
