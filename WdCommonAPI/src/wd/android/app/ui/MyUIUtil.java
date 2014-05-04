package wd.android.app.ui;

import wd.android.custom.MainApp;
import android.widget.Toast;

public class MyUIUtil {
	public static void showToast(int resId) {
		Toast.makeText(MainApp.getApp(), resId, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(CharSequence msg) {
		Toast.makeText(MainApp.getApp(), msg, Toast.LENGTH_SHORT).show();
	}
}
