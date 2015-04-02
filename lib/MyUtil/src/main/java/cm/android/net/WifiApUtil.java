package cm.android.net;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

import cm.java.util.ObjectProxy;

public class WifiApUtil {

    public static final int WIFI_AP_STATE_DISABLING = 10;

    /**
     * Wi-Fi AP is disabled.
     */
    public static final int WIFI_AP_STATE_DISABLED = 11;

    /**
     * Wi-Fi AP is currently being enabled. The state will change to
     * {@link #WIFI_AP_STATE_ENABLED} if it finishes successfully.
     */
    public static final int WIFI_AP_STATE_ENABLING = 12;

    /**
     * Wi-Fi AP is enabled.
     */
    public static final int WIFI_AP_STATE_ENABLED = 13;


    private WifiApUtil() {
    }

    public static void openWifiAp(WifiManager wifiManager, String ssid, String passwd) {
        WifiConfiguration config = createWifiApConfig(ssid, passwd);
        setWifiApEnabled(wifiManager, config, true);
    }

    public static WifiConfiguration createWifiApConfig(String ssid, String passwd) {
        WifiConfiguration netConfig = new WifiConfiguration();

//        netConfig.SSID = WifiUtil.getSSID(ssid);
//        netConfig.preSharedKey = WifiUtil.getWifiString(passwd);

        netConfig.SSID = ssid;
        netConfig.preSharedKey = passwd;

        netConfig.allowedAuthAlgorithms
                .set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement
                .set(WifiConfiguration.KeyMgmt.WPA_PSK);
        netConfig.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.CCMP);
        netConfig.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.TKIP);
        netConfig.allowedGroupCiphers
                .set(WifiConfiguration.GroupCipher.CCMP);
        netConfig.allowedGroupCiphers
                .set(WifiConfiguration.GroupCipher.TKIP);

        return netConfig;
    }

    public static void closeWifiAp(WifiManager wifiManager) {
        ObjectProxy proxy = new ObjectProxy(wifiManager);
        Method method = proxy.getMethod("getWifiApConfiguration");
        WifiConfiguration config = proxy.doMethod(method);
        setWifiApEnabled(wifiManager, config, false);
    }

    public static boolean setWifiApEnabled(WifiManager wifiManager, WifiConfiguration config,
            boolean enable) {
        if (enable && wifiManager.isWifiEnabled()) {
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            wifiManager.setWifiEnabled(false);
        }

        ObjectProxy proxy = new ObjectProxy(wifiManager);
        Method method = proxy.getMethod("setWifiApEnabled", config.getClass(), Boolean.TYPE);
        Boolean result = proxy.doMethod(method, config, enable);
        if (result != null) {
            return result;
        }
        return false;
    }

    public static boolean isWifiApEnabled(WifiManager wifiManager) {
        ObjectProxy proxy = new ObjectProxy(wifiManager);
        Method method = proxy.getMethod("isWifiApEnabled");
        Boolean result = proxy.doMethod(method);
        if (result != null) {
            return result;
        }
        return false;
    }

    public static int getWifiApState(WifiManager wifiManager) {
        ObjectProxy proxy = new ObjectProxy(wifiManager);
        Method method = proxy.getMethod("getWifiApState");
        Integer result = proxy.doMethod(method);
        if (result != null) {
            return result;
        }
        return 0;
    }

}

