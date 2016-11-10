package cm.android.sdk.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

public class PermissionUtil {
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (denied(context, permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean denied(Context context, String permission) {
//        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
        return PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

//    public static boolean requestPermissions(Context context, String[] permissions, @StringRes int text) {
//        boolean res = true;
//
//        if (!EnvironmentUtil.SdkUtil.has(Build.VERSION_CODES.M)) {
//            return true;
//        }
//
//        if (!checkDenied(context, permissions)) {
//            return true;
//        }
//
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.addCategory(Intent.CATEGORY_DEFAULT);
//        i.setData(Uri.parse("package:" + context.getPackageName()));
//        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                .setAutoCancel(true)
//                .setColor(Color.RED)
////                .setSmallIcon(context.getAp)
////                .setSmallIcon(R.drawable.ic_notification_icon)
////                .setContentTitle(context.getString(R.string.notification_title))
//                .setTicker(context.getString(text))
//                .setContentIntent(pi)
//                .setContentText(context.getString(text));
//        manager.notify(NOTIFICATION_ID, mBuilder.build());
//        return false;
//    }
}
