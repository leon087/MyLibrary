package cm.android.frameworkext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;

import java.net.InetAddress;
import java.util.Iterator;

public class NetworkUtilExt {

    /**
     * 设置gprs状态，需要系统权限签名
     * <p/>
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
     */
    public static boolean setMobileDataEnabled(Context context, boolean enabled) {
        ConnectivityManager cMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isOpen = cMgr.getMobileDataEnabled();

        if (isOpen == !enabled) {
            cMgr.setMobileDataEnabled(enabled);
        }
        return isOpen;
    }

    /**
     * Returns the default link's IP addresses, if any, taking into account IPv4
     * and IPv6 style addresses.
     *
     * @param context the application context
     * @return the formatted and comma-separated IP addresses, or null if none.
     */
    public static String getDefaultIpAddresses(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        LinkProperties prop = cm.getActiveLinkProperties();
        return formatIpAddresses(prop);
    }

    private static String formatIpAddresses(LinkProperties prop) {
        if (prop == null) {
            return null;
        }
        Iterator<InetAddress> iter = prop.getAddresses().iterator();
        // If there are no entries, return null
        if (!iter.hasNext()) {
            return null;
        }
        // Concatenate all available addresses, comma separated
        String addresses = "";
        while (iter.hasNext()) {
            addresses += iter.next().getHostAddress();
            if (iter.hasNext()) {
                addresses += ", ";
            }
        }
        return addresses;
    }
}
