package cm.android.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DeviceUtil {
    private static final Logger logger = LoggerFactory.getLogger(DeviceUtil.class);

    private DeviceUtil() {
    }

    public static String getDeviceFeatures(Context ctx) {
        return getIdentifiers(ctx) + getSystemFeatures()
                + getScreenFeatures(ctx);
    }

    public static String getIdentifiers(Context ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append(getPair("serial", Build.SERIAL));
        sb.append(getPair("android_id", getAndroidId(ctx)));
        TelephonyManager tel = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        sb.append(getPair("sim_country_iso", tel.getSimCountryIso()));
        sb.append(getPair("network_operator_name", tel.getNetworkOperatorName()));
        // sb.append(getPair("unique_id", Crypto.md5(sb.toString())));
        return sb.toString();
    }

    public static String getSystemFeatures() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPair("android_release", Build.VERSION.RELEASE));
        sb.append(getPair("android_sdk_int", "" + Build.VERSION.SDK_INT));
        sb.append(getPair("device_cpu_abi", Build.CPU_ABI));
        sb.append(getPair("device_model", Build.MODEL));
        sb.append(getPair("device_manufacturer", Build.MANUFACTURER));
        sb.append(getPair("device_board", Build.BOARD));
        sb.append(getPair("device_fingerprint", Build.FINGERPRINT));
        sb.append(getPair("device_cpu_feature", CPU.getFeatureString()));
        return sb.toString();
    }

    public static String getScreenFeatures(Context ctx) {
        StringBuilder sb = new StringBuilder();
        DisplayMetrics disp = ctx.getResources().getDisplayMetrics();
        sb.append(getPair("screen_density", "" + disp.density));
        sb.append(getPair("screen_density_dpi", "" + disp.densityDpi));
        sb.append(getPair("screen_height_pixels", "" + disp.heightPixels));
        sb.append(getPair("screen_width_pixels", "" + disp.widthPixels));
        sb.append(getPair("screen_scaled_density", "" + disp.scaledDensity));
        sb.append(getPair("screen_xdpi", "" + disp.xdpi));
        sb.append(getPair("screen_ydpi", "" + disp.ydpi));
        return sb.toString();
    }

    private static String getPair(String key, String value) {
        key = key == null ? "" : key.trim();
        value = value == null ? "" : value.trim();
        return "&" + key + "=" + value;
    }

    public static int getSdkInt() {
        try {
            int j = Build.VERSION.class.getField("SDK_INT").getInt(null);
            return j;
        } catch (Exception e1) {
            try {
                int i = Integer.parseInt((String) Build.VERSION.class.getField("SDK").get(null));
                return i;
            } catch (Exception e2) {
                logger.error("", e2);
            }
        }
        return 2;
    }

    /**
     * 获取设备号
     *
     * @return
     */
    @TargetApi(3)
    public static String getAndroidId(Context context) {
        return Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取Mac地址
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (null != info) {
            return info.getMacAddress();
        }
        return null;
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            logger.error("", ex);
        }
        return null;
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        return imei;
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tm.getSubscriberId();
        return imsi;
    }
}
