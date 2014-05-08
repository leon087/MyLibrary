package wd.android.util.sdk;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public abstract class MyWeakHandler {

	private static class MyHandler extends Handler {
		private WeakReference<MyWeakHandler> mOuter;

		public MyHandler(MyWeakHandler callback) {
			mOuter = new WeakReference<MyWeakHandler>(callback);
		}

		@Override
		public final void handleMessage(Message msg) {
			MyWeakHandler outer = mOuter.get();
			if (outer != null) {
				outer.handleMessage(msg);
			}
		}
	}

	private final MyHandler handler = new MyHandler(this);

	abstract void handleMessage(Message msg);

	public void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	// void xx(){
	// handler.sendEmptyMessage(what);
	// handler.sendEmptyMessageAtTime(what, uptimeMillis)
	// handler.sendEmptyMessageDelayed(what, delayMillis)
	// }
}
