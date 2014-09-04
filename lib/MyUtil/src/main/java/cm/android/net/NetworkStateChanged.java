package cm.android.net;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import cm.android.sdk.content.BaseBroadcastReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkStateChanged {
    private static final Logger logger = LoggerFactory.getLogger(NetworkStateReceiver.class);

    private static final NetworkChangeObserver defNetworkChangeObserver = new NetworkChangeObserver();
    private NetworkStateReceiver receiver = new NetworkStateReceiver();

    private NetworkChangeObserver observer = defNetworkChangeObserver;

    public void init(Context context, NetworkChangeObserver observer) {
        receiver.registerReceiver(context);
        registerObserver(observer);
    }

    public void deInit() {
        receiver.unRegisterReceiver();
        observer = null;
    }

    /**
     * 注册网络连接观察者
     */
    private synchronized void registerObserver(NetworkChangeObserver observer) {
        if (null == observer) {
            return;
        }
        this.observer = observer;
    }

    private synchronized void notifyObserver(boolean isNetworkAvailable, NetworkUtil.NetType netType) {
        if (isNetworkAvailable) {
            observer.onConnect(netType);
        } else {
            observer.onDisConnect();
        }
    }

    public class NetworkStateReceiver extends BaseBroadcastReceiver {
        private final static String ANDROID_NET_CHANGE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;

        @Override
        public IntentFilter createIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ANDROID_NET_CHANGE_ACTION);
            return filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
                logger.info("network changed.");
                boolean networkAvailable = NetworkUtil.isConnected(context);
                if (!networkAvailable) {
                    logger.info("network unavailable.");
                } else {
                    logger.info("network available.");
                }
                NetworkUtil.NetType netType = NetworkUtil.getAPNType(context);
                notifyObserver(networkAvailable, netType);
            }
        }
    }

    public static class NetworkChangeObserver {
        /**
         * 网络连接连接时调用
         */
        public void onConnect(NetworkUtil.NetType type) {

        }

        /**
         * 当前没有网络连接
         */
        public void onDisConnect() {

        }
    }
}