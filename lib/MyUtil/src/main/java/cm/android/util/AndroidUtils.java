package cm.android.util;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cm.android.applications.AppUtil;
import cm.java.util.ObjectUtil;
import cm.java.util.Utils;

/**
 * 常用工具类
 */
public final class AndroidUtils {

    private static final Logger logger = LoggerFactory.getLogger(AndroidUtils.class);

    private AndroidUtils() {
    }

    public static boolean isEmpty(Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * 桩模块(Stub), 获取假数据，用于各个子功能单元测试
     */
    public static Map<String, Object> getStubFile(Context cxt,
            String stubFileName) {
        if (Utils.isEmpty(stubFileName)) {
            return ObjectUtil.newHashMap();
        }
        Map<String, Object> stubMap = null;
        InputStream inputStream = null;
        // AssetManager assetManager = StoreApp.getApp().getAssets();

        try {
            AssetManager assetManager = cxt.getAssets();
            inputStream = assetManager.open(stubFileName);
            byte[] bytes = cm.java.util.IoUtil.read(inputStream);
            stubMap = JSON.parseObject(bytes, Map.class);
        } catch (Exception e) {
            logger.error("stubFileName = " + stubFileName, e);
        } finally {
            cm.java.util.IoUtil.closeQuietly(inputStream);
        }

        // TODO:
        if (stubMap == null) {
            logger.error("stubMap = null");
            return ObjectUtil.newHashMap();
        }
        return stubMap;
    }

    public static String getVersionCode(Context context) {
        PackageInfo packageInfo = AppUtil.getPackageInfo(
                context.getPackageManager(), context.getPackageName());
        if (packageInfo == null) {
            return "";
        }
        return String.valueOf(packageInfo.versionCode);
    }

    public static String getVersionName(Context context) {
        PackageInfo packageInfo = AppUtil.getPackageInfo(
                context.getPackageManager(), context.getPackageName());
        if (packageInfo == null) {
            return "";
        }
        return String.valueOf(packageInfo.versionName);
    }

    public static String getSystemProperties(String key) {
        String str = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            str = (String) get.invoke(c, key);
        } catch (Exception e) {
        }

        logger.debug("getSystemProperties key = {},str = {}", key, str);
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * 加载Properties
     */
    public static Properties loadProperties(Context context,
            String propertiesName) {
        Properties props = new Properties();
        try {
            int id = context.getResources().getIdentifier(propertiesName,
                    "raw", context.getPackageName());
            InputStream is = context.getResources().openRawResource(id);
            props.load(is);
        } catch (Exception e) {
            logger.error("Could not find the properties file.", e);
            // e.printStackTrace();
        }
        return props;
    }


    /**
     * 获取classes.dex文件CRC值
     */
    @TargetApi(8)
    public static long getDexCrc(Context context) {
        long crc = 0;
        try {
            ZipFile zf = new ZipFile(context.getApplicationContext().getPackageCodePath());
            ZipEntry ze = zf.getEntry("classes.dex");
            crc = ze.getCrc();
            return crc;
        } catch (IOException e) {
            logger.error("", e);
        }
        return 0;
    }

    public static boolean isDebuggable(ApplicationInfo applicationInfo) {
        if ((applicationInfo.flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            return true;
        }
        return false;
    }

    public static boolean isDebuggable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return isDebuggable(info);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDebuggable(Context context, String packageName) {
        PackageInfo pkginfo = AppUtil.getPackageInfo(context.getPackageManager(), packageName,
                PackageManager.GET_ACTIVITIES);
        if (pkginfo != null) {
            return isDebuggable(pkginfo.applicationInfo);
        }

        return false;
    }

    public static boolean reboot(Context context) {
        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            pm.reboot(context.getPackageName());
            return true;
        } catch (Exception e) {
            logger.error("reboot error:" + e.getMessage(), e);
            return false;
        }
    }

    public static void killProcess(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (EnvironmentUtil.SdkUtil.hasFroyo()) {
            am.killBackgroundProcesses(context.getPackageName());
        } else {
            am.restartPackage(context.getPackageName());
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static void transferData(Bundle oldBundle, Bundle newBundle) {
        if (oldBundle != null) {
            newBundle.putAll(oldBundle);
        }
    }

    public static void transferData(Intent oldIntent, Intent newIntent) {
        if (oldIntent == null) {
            return;
        }

        newIntent.putExtras(oldIntent);
    }

    public static boolean setAdbEnabled(Context context, int value) {
        try {
            return Settings.Secure.putInt(context.getContentResolver(), Settings.Global.ADB_ENABLED,
                    value);
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
