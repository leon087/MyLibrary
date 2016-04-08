package cm.android.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.Iterator;
import java.util.List;

import cm.java.util.Utils;

@SuppressWarnings("MissingPermission")
public class WifiUtil {

    private static final Logger logger = LoggerFactory.getLogger("WifiUtil");

    /**
     * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static boolean removeNetwork(Context context, int networkId, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        if (Utils.isEmpty(list)) {
            return true;
        }

        String SSID = getSSID(ssid);

        Iterator<WifiConfiguration> iterator = list.iterator();

        while (iterator.hasNext()) {
            WifiConfiguration wifiConfiguration = iterator.next();
            if (wifiConfiguration.networkId == networkId && wifiConfiguration.SSID.equals(SSID)) {
                return wifiManager.removeNetwork(wifiConfiguration.networkId);
            }
        }
        return true;
    }

    public static int addNetwork(Context context, WifiConfig wifiConfig, boolean auto) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int netId = addNetwork(wifiManager, wifiConfig);
        if (auto) {
            wifiManager.enableNetwork(netId, true);
        }

        return netId;
    }

    public static int addNetwork(WifiManager wifiManager, WifiConfig wifiConfig) {
        WifiConfiguration wifiConfiguration = createWifiInfo(wifiConfig);
        int netId = wifiManager.addNetwork(wifiConfiguration);
        return netId;
    }

    public static WifiConfiguration createWifiInfo(WifiConfig wifiConfig) {
        logger.info("SSID = {},password = {},type = {}", getSSID(wifiConfig.ssid),
                wifiConfig.password, wifiConfig.wifiType);

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = getSSID(wifiConfig.ssid);
        config.hiddenSSID = wifiConfig.hiddenSSID;
        config.status = WifiConfiguration.Status.ENABLED;

        // 分为三种情况：1没有密码2用wep加密3用wpa加密
        if (wifiConfig.wifiType == WifiType.NOPASS) {
//            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
        } else if (wifiConfig.wifiType == WifiType.WEP) {
            config.wepKeys[0] = "\"" + wifiConfig.password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (wifiConfig.wifiType == WifiType.WPA) {
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            config.preSharedKey = "\"" + wifiConfig.password + "\"";
        }

        return config;
    }

    public static WifiConfiguration exsits(WifiManager wifiManager, String ssid) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (Utils.isEmpty(existingConfigs)) {
            return null;
        }

        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals(getSSID(ssid))) {
                return existingConfig;
            }
        }
        return null;
    }

    public static enum WifiType {
        INVALID(0), NOPASS(1), WEP(2), WPA(3), _802_1xEAP(4);

        private int type;

        private WifiType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public boolean equals(int type) {
            return this.type == type;
        }

        public static WifiType getType(int type) {
            switch (type) {
                case 1:
                    return NOPASS;
                case 2:
                    return WEP;
                case 3:
                    return WPA;
                case 4:
                    return _802_1xEAP;
                default:
                    return INVALID;
            }
        }
    }

    public static class WifiConfig {

        public WifiType wifiType;

        public String password;

        public boolean hiddenSSID;

        public String ssid;
    }

    public static String getSSID(String ssid) {
        String SSID = "\"" + ssid + "\"";
        return SSID;
    }

    public static String getWifiString(String str) {
        String result = "\"" + str + "\"";
        return result;
    }
}
