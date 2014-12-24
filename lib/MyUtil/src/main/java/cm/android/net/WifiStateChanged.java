package cm.android.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(WifiStateChanged.class);

    private Context mContext;

    private NetworkConnectChangedReceiver wifiReceiver = new NetworkConnectChangedReceiver();

    private IWifiStateListener wifiListener;

    public WifiStateChanged(IWifiStateListener wifiListener) {
        this.wifiListener = wifiListener;
    }

    public void register(Context context) {
        this.mContext = context;
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

    private void handleWifiStateChanged(int state, Context context, Intent intent) {
        switch (state) {
            case WifiManager.WIFI_STATE_ENABLING:
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                wifiListener.onConnected(context, intent);
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                wifiListener.onDisConnected(context, intent);
                break;
            default:
                break;
        }
    }

    public interface IWifiStateListener {

        void onConnected(Context context, Intent intent);

        void onDisConnected(Context context, Intent intent);
    }

    private class NetworkConnectChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            logger.info("intent.getAction() = " + intent.getAction());

            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent
                    .getAction())) {// 这个监听wifi的连接状态
                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    State state = networkInfo.getState();
                    if (state == State.CONNECTED) {
                        wifiListener.onConnected(context, intent);
                    } else {
                        wifiListener.onDisConnected(context, intent);
                    }
                }
            }
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
                    .getAction())) {// 这个监听网络连接的设置，包括wifi和移动数据 的打开和关闭
                NetworkInfo info = intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    logger.error("info.getType() = " + info.getType());
                    if (NetworkInfo.State.CONNECTED == info.getState()) {
                        logger.error("info.getState() = CONNECTED");
                    } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        if (NetworkInfo.State.DISCONNECTED == info.getState()
                                || NetworkInfo.State.DISCONNECTING == info
                                .getState()) {
                            // showWifiDisconnected(context);
                            logger.error(
                                    "info.getState() = " + info.getState());
                        }
                    }
                }
            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent
                    .getAction())) {
                handleWifiStateChanged(intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN), context, intent);
            }
        }
    }
}
