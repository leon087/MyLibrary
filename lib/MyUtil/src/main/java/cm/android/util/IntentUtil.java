package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.webkit.MimeTypeMap;

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
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (null != intent) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            logger.error("packageName = " + packageName, e);
        }
        return false;
    }

//    public static void startActivitySafely(Activity activity, Intent intent) {
//        try {
//            activity.startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            logger.error("intent = " + intent, e);
//        } catch (SecurityException e) {
//            logger.error("intent = " + intent, e);
//        }
//    }

    public static void startActivitySafely(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            logger.error("intent = " + intent, e);
        } catch (SecurityException e) {
            logger.error("intent = " + intent, e);
        }
    }

    /**
     * 发送安装请求，调用系统安装界面
     */
    public static boolean installPackage(Context context, Uri packageURI) {
        File file = new File(packageURI.getPath());
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        Intent apkintent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT >= 23) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension("apk");
        }
        apkintent.setDataAndType(packageURI, type);
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

    public static void sendBroadcastSyncLocal(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent);
    }

    public static void sendBroadcastInternal(Context context, Intent intent, String permission) {
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent, permission);
    }
}
