package cm.android.app.ui;

import android.widget.Toast;
import cm.android.custom.MainApp;

public class MyUIUtil {
	public static void showToast(int resId) {
		Toast.makeText(MainApp.getApp(), resId, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(CharSequence msg) {
		Toast.makeText(MainApp.getApp(), msg, Toast.LENGTH_SHORT).show();
	}
}
