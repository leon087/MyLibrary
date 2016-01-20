package cm.android.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.net.ConnectivityManagerCompat;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cm.android.util.EnvironmentUtil;
import cm.java.util.ObjectProxy;

public class NetworkUtil {

    private static final Logger logger = LoggerFactory.getLogger(NetworkUtil.class);

    /**
     * 取得网络类型
     */
    public static int getNetWorkType(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType();
    }

    /**
     * 对大数据传输时，需要调用该方法做出判断，如果流量敏感，应该提示用户
     *
     * @return true表示流量敏感，false表示不敏感
     */
    public static boolean isActiveNetworkMetered(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return ConnectivityManagerCompat.isActiveNetworkMetered(cm);
    }

    /**
     * 检查当前是否连接
     *
     * @return true表示当前网络处于连接状态，否则返回false
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            boolean wifiConnected = info.getType() == ConnectivityManager.TYPE_WIFI;
            return wifiConnected;
        }
        return false;

        // NetworkInfo networkInfo = connMgr
        // .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // if (null != networkInfo) {
        // boolean isWifiConn = networkInfo.isConnected();
        // return isWifiConn;
        // }
        // return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            boolean mobileConnected = info.getType() == ConnectivityManager.TYPE_MOBILE;
            return mobileConnected;
        }
        return false;

        // NetworkInfo networkInfo = connMgr
        // .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // if (null != networkInfo) {
        // boolean isMobileConn = networkInfo.isConnected();
        // return isMobileConn;
        // }
        // return false;
    }

    /**
     * 获取当前网络连接的类型信息
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
     */
    public static NetType getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetType.NONE_NET;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if ("cmnet".equalsIgnoreCase(networkInfo.getExtraInfo())) {
                return NetType.CMNET;
            } else {
                return NetType.CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE_NET;
    }

    public static int getNetWorkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo mNetworkInfo = connMgr.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1000;
    }

    public static boolean isConnectedWap(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (netInfo != null && netInfo.getExtraInfo() != null) {
            if (netInfo.getExtraInfo().trim().toLowerCase().contains("wap")
                    && netInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static boolean wifiConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null) {
            if (info.isConnected()
                    && info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else {
                NetworkInfo info1 = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (info1 != null && info1.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 网络类型
     */
    public static enum NetType {
        WIFI, CMNET, CMWAP, NONE_NET
    }

    /**
     * 设置gprs状态
     * <p/>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
     * <uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS"/>
     * <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
     * <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>
     */
    public static void setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        ObjectProxy proxy = new ObjectProxy(cm);
        Method setMobileDataEnabledMethod = proxy
                .getMethod("setMobileDataEnabled", Boolean.TYPE);
        if (setMobileDataEnabledMethod != null) {
            proxy.doMethod(setMobileDataEnabledMethod, enabled);
        }

//            Object iConnectivityManager = ReflectUtil.getFieldValue(conman, "mService");
//        Field iConnectivityManagerField;
//        try {
//            final Class conmanClass = Class.forName(conman.getClass().getName());
//            iConnectivityManagerField = conmanClass.getDeclaredField("mService");
//            iConnectivityManagerField.setAccessible(true);
//            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
//
//            final Class iConnectivityManagerClass = Class
//                    .forName(iConnectivityManager.getClass().getName());
//            final Method setMobileDataEnabledMethod = iConnectivityManagerClass
//                    .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//            setMobileDataEnabledMethod.setAccessible(true);
//            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
//        } catch (SecurityException e) {
//            logger.error("", e);
//        } catch (NoSuchFieldException e) {
//            logger.error("", e);
//        } catch (ClassNotFoundException e) {
//            logger.error("", e);
//        } catch (IllegalArgumentException e) {
//            logger.error("", e);
//        } catch (IllegalAccessException e) {
//            logger.error("", e);
//        } catch (NoSuchMethodException e) {
//            logger.error("", e);
//        } catch (InvocationTargetException e) {
//            logger.error("", e);
//        }
    }

    public static boolean getMobileDataEnabled(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        ObjectProxy proxy = new ObjectProxy(cm);
        Method getMobileDataEnabledMethod = proxy.getMethod("getMobileDataEnabled");
        if (getMobileDataEnabledMethod != null) {
            return proxy.doMethod(getMobileDataEnabledMethod);
        }
        return true;
    }


    /**
     * android.permission.MODIFY_PHONE_STATE (only system apps can use that)
     */
    public static void setMobileDataEnabledCompat(Context context, boolean enabled) {
        if (context == null) {
            return;
        }

        if (!EnvironmentUtil.SdkUtil.hasLollipop()) {
            setMobileDataEnabled(context, enabled);
        }

        try {
            TelephonyManager telephonyService = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass()
                    .getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(telephonyService, enabled);
            }
        } catch (Exception ex) {
            logger.error("Error setting mobile data state", ex);
        }
    }

    public static boolean getMobileDataEnabledCompat(Context context) {

        if (!EnvironmentUtil.SdkUtil.hasLollipop()) {
            return getMobileDataEnabled(context);
        }

        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);

            Method getMobileDataEnabledMethod = telephonyService.getClass()
                    .getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod) {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod
                        .invoke(telephonyService);

                return mobileDataEnabled;
            }
        } catch (Exception ex) {
            logger.error("Error getting mobile data state", ex);
        }

        return false;
    }


    /**
     * need rooted devices
     */
    public static void setMobileDataEnabledCompat1(Context context, boolean enabled) {
        if (context == null) {
            return;
        }

        if (!EnvironmentUtil.SdkUtil.hasLollipop()) {
            setMobileDataEnabled(context, enabled);
        }

        String command = null;
        int state = 0;
        try {
            // Get the current state of the mobile network.
            state = getMobileDataEnabledCompat1(context) ? 0 : 1;
            // Get the value of the "TRANSACTION_setDataEnabled" field.
            String transactionCode = getTransactionCode(context);
            // Android 5.1+ (API 22) and later.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                SubscriptionManager mSubscriptionManager = (SubscriptionManager) context
                        .getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                // Loop through the subscription list i.e. SIM list.
                for (int i = 0; i < mSubscriptionManager.getActiveSubscriptionInfoCountMax(); i++) {
                    if (transactionCode != null && transactionCode.length() > 0) {
                        // Get the active subscription ID for a given SIM card.
                        int subscriptionId = mSubscriptionManager.getActiveSubscriptionInfoList()
                                .get(i).getSubscriptionId();
                        // Execute the command via `su` to turn off
                        // mobile network for a subscription service.
                        command = "service call phone " + transactionCode + " i32 " + subscriptionId
                                + " i32 " + state;
                        executeCommandViaSu("-c", command);
                    }
                }
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                // Android 5.0 (API 21) only.
                if (transactionCode != null && transactionCode.length() > 0) {
                    // Execute the command via `su` to turn off mobile network.
                    command = "service call phone " + transactionCode + " i32 " + state;
                    executeCommandViaSu("-c", command);
                }
            }
        } catch (Exception e) {
            // Oops! Something went wrong, so we throw the exception here.
            throw e;
        }
    }

    public static boolean getMobileDataEnabledCompat1(Context context) {

        if (!EnvironmentUtil.SdkUtil.hasLollipop()) {
            return getMobileDataEnabled(context);
        }

        return Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
    }

    private static String getTransactionCode(Context context) {
        try {
            final TelephonyManager mTelephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            final Class<?> mTelephonyClass = Class.forName(mTelephonyManager.getClass().getName());
            final Method mTelephonyMethod = mTelephonyClass.getDeclaredMethod("getITelephony");
            mTelephonyMethod.setAccessible(true);
            final Object mTelephonyStub = mTelephonyMethod.invoke(mTelephonyManager);
            final Class<?> mTelephonyStubClass = Class.forName(mTelephonyStub.getClass().getName());
            final Class<?> mClass = mTelephonyStubClass.getDeclaringClass();
            final Field field = mClass.getDeclaredField("TRANSACTION_setDataEnabled");
            field.setAccessible(true);
            return String.valueOf(field.getInt(null));
        } catch (Exception e) {
            // The "TRANSACTION_setDataEnabled" field is not available,
            // or named differently in the current API level, so we throw
            // an exception and inform users that the method is not available.
            logger.error("The TRANSACTION_setDataEnabled field is not available : ", e);
        }
        return null;
    }

    private static void executeCommandViaSu(String option, String command) {
        boolean success = false;
        String su = "su";
        for (int i = 0; i < 3; i++) {
            // Default "su" command executed successfully, then quit.
            if (success) {
                break;
            }
            // Else, execute other "su" commands.
            if (i == 1) {
                su = "/system/xbin/su";
            } else if (i == 2) {
                su = "/system/bin/su";
            }
            try {
                // Execute command as "su".
                Runtime.getRuntime().exec(new String[]{su, option, command});
            } catch (IOException e) {
                success = false;
                // Oops! Cannot execute `su` for some reason.
                // Log error here.
            } finally {
                success = true;
            }
        }
    }
}
