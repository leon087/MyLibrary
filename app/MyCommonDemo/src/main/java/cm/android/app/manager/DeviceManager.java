package cm.android.app.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import cm.android.applications.AppUtil;
import cm.android.custom.MainApp;
import cm.android.framework.core.BaseApp;
import cm.android.util.MyLog;

public class DeviceManager {
    private static final int versionCode;
    private static final String versionName;
    private static final String appVersion;

    private static String USER_AGENT;
    private static final String[] FACTURERS = new String[]{"MOTOROLA",
            "MOTO", "DOPOD", "HTC", "SAMSUNG", "SONYERICSSON", "SONY ERICSSON",
            "DELL", "ZTE", "HUAWEI", "TCL", "MEIZU", "LG", "LENOVO", "ASUS",
            "ACER", "YULONG", "CoolPad", "Toshiba", "Philips", "NEWLAND",
            "LGE", "K-Touch", "Haier", "HS", "Amoi", "Hisense"};

    static {
        PackageInfo packageInfo = AppUtil.getPackageInfo(BaseApp.getApp()
                .getPackageManager(), BaseApp.getApp().getPackageName());
        versionCode = packageInfo.versionCode;
        versionName = packageInfo.versionName;
        appVersion = getAppVersion(versionCode);

        getUAConfig("android");
    }

    public static int getVersionCode() {
        return versionCode;
    }

    public static String getVersionName() {
        return versionName;
    }

    public static String getPackageName() {
        return BaseApp.getApp().getPackageName();
    }

    public static String getUserAgent() {
        return USER_AGENT;
    }

    public static String getSimOperator() {
        TelephonyManager telManager = (TelephonyManager) MainApp.getApp()
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telManager != null
                && telManager.getSimState() == TelephonyManager.SIM_STATE_READY
                && telManager.getSubscriberId() != null
                && !"".equals(telManager.getSubscriberId())) {
            return telManager.getSimOperator();
        }
        return "";
    }

    private static void getUAConfig(String sysVersion) {
        boolean flag = false;
        for (String fact : FACTURERS) {
            if (Build.MANUFACTURER != null
                    && Build.MANUFACTURER.equalsIgnoreCase(fact)) {
                if (!Build.MODEL.toLowerCase().contains(
                        Build.MANUFACTURER.toLowerCase())) {
                    USER_AGENT = Build.MANUFACTURER + "_" + Build.MODEL;
                    flag = true;
                }
                break;
            }
            if (Build.BRAND != null && Build.BRAND.equalsIgnoreCase(fact)) {
                if (!Build.MODEL.toLowerCase().contains(
                        Build.BRAND.toLowerCase())) {
                    USER_AGENT = Build.BRAND + "_" + Build.MODEL;
                    flag = true;
                }
                break;
            }
        }
        if (!flag) {
            USER_AGENT = Build.MODEL;
        }
        USER_AGENT = USER_AGENT.toLowerCase().replaceAll(" ", "_");
        USER_AGENT = USER_AGENT + "_" + sysVersion;
        MyLog.i("USER_AGENT = " + USER_AGENT);
    }

    private static String getAppVersion(int versionCode) {
        StringBuilder curVersion = new StringBuilder();
        int v = versionCode;
        if (v >= 2000000) {
            for (int i = 0; i < 4; i++) {
                int j = v % 100;
                v = v / 100;
                curVersion.insert(0, "." + j);
            }
        }
        MyLog.i("-------current  version-----" + curVersion);
        if (null != curVersion && curVersion.length() > 1) {
            return curVersion.substring(1);
        } else {
            return "";
        }
    }

    /**
     * 获取应用版本
     */
    public static String getAppVersion() {
        return appVersion;
    }

}
