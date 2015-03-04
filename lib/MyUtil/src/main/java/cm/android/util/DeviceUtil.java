package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import cm.java.util.ReflectUtil;
import cm.java.util.Utils;

public class DeviceUtil {

    private static final Logger logger = LoggerFactory.getLogger(DeviceUtil.class);

    private DeviceUtil() {
    }

    public static String getDeviceFeatures(Context ctx) {
        return getIdentifiers(ctx) + getSystemFeatures()
                + getScreenFeatures(ctx) + getTele(ctx);
    }

    public static String getIdentifiers(Context ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append(getPair("\nserial", Build.SERIAL));
        sb.append(getPair("\nandroid_id", getAndroidId(ctx)));
        TelephonyManager tel = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        sb.append(getPair("\nsim_country_iso", tel.getSimCountryIso()));
        sb.append(getPair("\nnetwork_operator_name", tel.getNetworkOperatorName()));
        // sb.append(getPair("unique_id", Crypto.md5(sb.toString())));
        return sb.toString();
    }

    public static String getTele(Context context) {
        StringBuilder sb = new StringBuilder();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        sb.append(getPair("getPhoneType", tm.getPhoneType() + ""));
        sb.append(getPair("getDeviceSoftwareVersion", tm.getDeviceSoftwareVersion() + ""));
        sb.append(getPair("getSimOperator", tm.getSimOperator() + ""));
        sb.append(getPair("getSimOperatorName", tm.getSimOperatorName() + ""));
        sb.append(getPair("getSimSerialNumber", tm.getSimSerialNumber() + ""));
        sb.append(getPair("getVoiceMailNumber", tm.getVoiceMailNumber() + ""));

        return sb.toString();
    }

    public static String getSystemFeatures() {
        StringBuilder sb = new StringBuilder();

        sb.append(getPair("\nMODEL", Build.MODEL));
        sb.append(getPair("\nMANUFACTURER", Build.MANUFACTURER));//api:4
        sb.append(getPair("\nBOARD", Build.BOARD));
        sb.append(getPair("\nFINGERPRINT", Build.FINGERPRINT));

        sb.append(getPair("\nBOOTLOADER", Build.BOOTLOADER));//api:8
        sb.append(getPair("\nBRAND", Build.BRAND));
        sb.append(getPair("\nCPU_ABI", Build.CPU_ABI));//api:4
        sb.append(getPair("\nCPU_ABI2", Build.CPU_ABI2));//api:8
        sb.append(getPair("\nDEVICE", Build.DEVICE));
        sb.append(getPair("\nDISPLAY", Build.DISPLAY));//api:3
        //sb.append(getPair("\ndevice_cpu_feature", Build.getRadioVersion()));//API:14
        sb.append(getPair("\nHARDWARE", Build.HARDWARE));//API:8
        sb.append(getPair("\nHOST", Build.HOST));
        sb.append(getPair("\nID", Build.ID));
        sb.append(getPair("\nPRODUCT", Build.PRODUCT));
        sb.append(getPair("\nSERIAL", Build.SERIAL));//api:9
        sb.append(getPair("\nTAGS", Build.TAGS));
        sb.append(getPair("\nTYPE", Build.TYPE));
        sb.append(getPair("\nUSER", Build.USER));
        sb.append(getPair("\nTIME", Build.TIME + ""));

        sb.append(getPair("\nVERSION.SDK_INT", "" + Build.VERSION.SDK_INT));//api:4
        sb.append(getPair("\nVERSION.CODENAME", Build.VERSION.CODENAME));//api:4
        sb.append(getPair("\nVERSION.RELEASE", Build.VERSION.RELEASE));
        sb.append(getPair("\nVERSION.INCREMENTAL", Build.VERSION.INCREMENTAL));
        return sb.toString();
    }

    public static String getScreenFeatures(Context ctx) {
        StringBuilder sb = new StringBuilder();
        DisplayMetrics disp = ctx.getResources().getDisplayMetrics();
        sb.append(getPair("\nscreen_density", "" + disp.density));
        sb.append(getPair("\nscreen_density_dpi", "" + disp.densityDpi));
        sb.append(getPair("\nscreen_height_pixels", "" + disp.heightPixels));
        sb.append(getPair("\nscreen_width_pixels", "" + disp.widthPixels));
        sb.append(getPair("\nscreen_scaled_density", "" + disp.scaledDensity));
        sb.append(getPair("\nscreen_xdpi", "" + disp.xdpi));
        sb.append(getPair("\nscreen_ydpi", "" + disp.ydpi));
        return sb.toString();
    }

    private static String getPair(String key, String value) {
        key = key == null ? "" : key.trim();
        value = value == null ? "" : value.trim();
        return "\n" + key + "=" + value;
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

    @TargetApi(8)
    public static String getBootloader() {
        if (EnvironmentUtil.SdkUtil.hasFroyo()) {
            return Build.BOOTLOADER;
        }
        return "";
    }

    @TargetApi(8)
    public static String getCpuAbi2() {
        try {
            String cpuAbi2 = (String) Build.class.getField("CPU_ABI2").get(new Build());
            return cpuAbi2;
        } catch (Exception e1) {
            return "";
        }
    }

    @TargetApi(8)
    public static String getHardware() {
        try {
            String hardware = ReflectUtil.getFieldValue(new Build(), "HARDWARE");
            return hardware;
        } catch (Exception e) {
            return "";
        }
    }

    @TargetApi(9)
    public static String getSerial() {
        try {
            String serial = ReflectUtil.getFieldValue(new Build(), "SERIAL");
            if (Utils.isEmpty(serial)) {
                logger.error("serial = " + serial);
                return "";
            }
            return serial;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取设备号
     */
    @TargetApi(3)
    public static String getAndroidId(Context context) {
        return Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static UUID getUUID(Context context) {
        String appTag = "111";
        return getUUID(context, appTag);
    }

    public static UUID getUUID(Context context, String appTag) {
        //需要通信模块
        String imei = getIMEI(context);

        String macAddress = getMacAddress(context);

        //可能相同或为null
        String serial = getSerial();

        int appTagHashCode = Math.abs(appTag.hashCode());
        int imeiHashCode = Math.abs(imei.hashCode());
        int macAddressHashCode = Math.abs(macAddress.hashCode());
        int serialHashCode = Math.abs(serial.hashCode());

        long mostSigBits = ((long) appTagHashCode) << 32 | imeiHashCode;
        long leastSigBits = ((long) macAddressHashCode) << 32 | serialHashCode;
        UUID deviceUuid = new UUID(mostSigBits, leastSigBits);
        return deviceUuid;
    }

    /**
     * 获取Mac地址
     */
    public static String getMacAddress(Context context) {
        String macAddr = "";
        try {
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (null != info) {
                String mac = info.getMacAddress();
                if (!Utils.isEmpty(mac)) {
                    macAddr = mac;
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return macAddr;
    }

    /**
     * 获取IP地址
     */
    public static String getIpAddress() {
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
     */
    public static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (Utils.isEmpty(imei)) {
                logger.error("imei = " + imei);
                return "";
            }
            return imei;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 获取IMSI
     */
    public static String getIMSI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = tm.getSubscriberId();
            if (Utils.isEmpty(imsi)) {
                logger.error("imsi = " + imsi);
                return "";
            }
            return imsi;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

    public static String getHostName() {
        try {
            Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append(Build.MANUFACTURER).append("_").append(Build.MODEL);
            return sb.toString();
        }
    }

    public static String getHostName2() {
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getDeclaredMethod("get", String.class);
            get.setAccessible(true);
            return get.invoke(null, "net.hostname").toString();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append(Build.MANUFACTURER).append("_").append(Build.MODEL);
            return sb.toString();
        }
    }

    public static int getAdbEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(),
                Settings.Global.ADB_ENABLED, 0);
    }
}
