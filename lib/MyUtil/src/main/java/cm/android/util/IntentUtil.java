package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import java.io.File;

import cm.java.util.Utils;

/**
 * 封装了一些Intent操作
 */
public class IntentUtil {

    private static final Logger logger = LoggerFactory.getLogger(IntentUtil.class);
    // private static Context sContext = StoreApp.getApp();
    //
    // public static void sendIntent(Class<?> cls, Bundle bundle, int flags) {
    // Intent intent = new Intent(sContext, cls);
    // if (null != bundle) {
    // intent.putExtras(bundle);
    // }
    // // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    // // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    // // intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
    // // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    // intent.addFlags(flags);
    // sContext.startActivity(intent);
    // }

    /**
     * 打开一个应用
     */
    public static boolean launchApp(Context context, String packageName) {
        try {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            if (null != intent) {
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            logger.error("packageName = " + packageName, e);
        }
        return false;
    }

    /**
     * 发送安装请求，调用系统安装界面
     */
    public static boolean installPackage(Context context, Uri packageURI) {
        File file = new File(packageURI.getPath());
        if (file == null || !file.exists() || !file.isFile()
                || file.length() <= 0) {
            return false;
        }

        // acti.finish();
        Intent apkintent = new Intent(Intent.ACTION_VIEW);
        apkintent.setDataAndType(packageURI,
                "application/vnd.android.package-archive");
        apkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkintent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.startActivity(apkintent);
        return true;
    }

    /**
     * 发送卸载应用请求，调用系统卸载界面
     */
    public static boolean deletePackage(Context context, String packageName) {
        if (Utils.isEmpty(packageName)) {
            return false;
        }

        Uri packageURI = Uri.parse("package:" + packageName);
        Intent apkintent = new Intent(Intent.ACTION_DELETE, packageURI);
        apkintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkintent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.startActivity(apkintent);
        return true;
    }

    public static void sendBroadcastInternal(Context context, Intent intent) {
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    public static void sendBroadcastLocal(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendBroadcastInternal(Context context, Intent intent, String permission) {
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent, permission);
    }
}
