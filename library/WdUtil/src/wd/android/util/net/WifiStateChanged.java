package wd.android.util.net;

import wd.android.util.util.MyLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

public class WifiStateChanged {

	private Context mContext;
	private NetworkConnectChangedReceiver wifiReceiver = new NetworkConnectChangedReceiver();
	private IWifiStateListener wifiListener;

	public WifiStateChanged(Context context, IWifiStateListener wifiListener) {
		mContext = context;
		this.wifiListener = wifiListener;
	}

	public void register() {
		// WIFI状态接收器
		IntentFilter filter = new IntentFilter();
		// filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		// filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(wifiReceiver, filter);
	}

	public void unRegister() {
		mContext.unregisterReceiver(wifiReceiver);
	}

	private class NetworkConnectChangedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			MyLog.i("intent.getAction() = " + intent.getAction());

			if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent
					.getAction())) {// 这个监听wifi的连接状态
				Parcelable parcelableExtra = intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (null != parcelableExtra) {
					NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
					State state = networkInfo.getState();
					if (state == State.CONNECTED) {
						wifiListener.onConnected();
					} else {
						wifiListener.onDisConnected();
					}
				}
			}
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
					.getAction())) {// 这个监听网络连接的设置，包括wifi和移动数据 的打开和关闭
				NetworkInfo info = intent
						.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
				if (info != null) {
					MyLog.e("info.getType() = " + info.getType());
					if (NetworkInfo.State.CONNECTED == info.getState()) {
						MyLog.e("info.getState() = CONNECTED");
					} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
						if (NetworkInfo.State.DISCONNECTED == info.getState()
								|| NetworkInfo.State.DISCONNECTING == info
										.getState()) {
							// showWifiDisconnected(context);
							MyLog.e(
									"info.getState() = " + info.getState());
						}
					}
				}
			} else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent
					.getAction())) {
				handleWifiStateChanged(intent.getIntExtra(
						WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_UNKNOWN));
			}
		}
	}

	private void handleWifiStateChanged(int state) {
		switch (state) {
		case WifiManager.WIFI_STATE_ENABLING:
			break;
		case WifiManager.WIFI_STATE_ENABLED:
			wifiListener.onConnected();
			break;
		case WifiManager.WIFI_STATE_DISABLING:
			break;
		case WifiManager.WIFI_STATE_DISABLED:
			wifiListener.onDisConnected();
			break;
		default:
			break;
		}
	}

	public interface IWifiStateListener {
		void onConnected();

		void onDisConnected();
	}
}
