package cm.android.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.telephony.TelephonyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NetworkUtil {

    private static final Logger logger = LoggerFactory.getLogger(NetworkUtil.class);

    // sim卡是否可读
    public static boolean isSimReady(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 对大数据传输时，需要调用该方法做出判断，如果流量敏感，应该提示用户
     *
     * @param context
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
     * @param context
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
     *
     * @param context
     * @return
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
     *
     * @param context
     * @return
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
     *
     * @param context
     * @return
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
     *
     * @param context
     * @return
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
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
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
     */
    public static void setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Field iConnectivityManagerField;
        try {
            final Class conmanClass = Class.forName(conman.getClass().getName());
            iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (SecurityException e) {
            logger.error("", e);
        } catch (NoSuchFieldException e) {
            logger.error("", e);
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        } catch (IllegalArgumentException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        } catch (NoSuchMethodException e) {
            logger.error("", e);
        } catch (InvocationTargetException e) {
            logger.error("", e);
        }
    }
}
