package wd.android.util.sdk;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public class WeakHandler extends Handler {
	// WeakReference to the outer class's instance.
	private WeakReference<WeakCallback> mOuter;

	// private WeakHandler() {
	// }
	//
	// private WeakHandler(Looper looper) {
	//
	// }

	public WeakHandler(WeakCallback callback) {
		mOuter = new WeakReference<WeakCallback>(callback);
	}

	@Override
	public final void handleMessage(Message msg) {
		WeakCallback outer = mOuter.get();
		if (outer != null) {
			outer.handleMessage(msg);
		}
	}

	public static interface WeakCallback {
		void handleMessage(Message msg);
	}
}
