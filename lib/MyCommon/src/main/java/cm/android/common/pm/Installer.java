package cm.android.common.pm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cm.java.util.IoUtil;

/**
 * The type Installer.
 */
public class Installer {
    private static final Logger logger = LoggerFactory.getLogger("pm");

    /**
     * The constant ACTION_INSTALL_COMPLETE.
     */
    public static final String ACTION_INSTALL_COMPLETE = "cm.android.intent.action.INSTALL_COMPLETE";
    /**
     * The constant ACTION_UNINSTALL_COMPLETE.
     */
    public static final String ACTION_UNINSTALL_COMPLETE = "cm.android.intent.action.UNINSTALL_COMPLETE";

    /**
     * Install.
     *
     * @param context     the context
     * @param packageName the package name
     * @param apkPath     the apk path
     */
    public static void install(Context context, String packageName, String apkPath) {
        logger.info("install:packageName = {},apkPath = {}", packageName, apkPath);

        InputStream in = null;
        OutputStream out = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PackageInstaller.Session session = null;
            try {
                in = new FileInputStream(new File(apkPath));

                PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
                PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                        PackageInstaller.SessionParams.MODE_FULL_INSTALL);
                //TODO ggg 通过apkPath读取
                params.setAppPackageName(packageName);

                // set params
                int sessionId = packageInstaller.createSession(params);
                session = packageInstaller.openSession(sessionId);
                out = session.openWrite("Installer", 0, -1);
                IoUtil.write(in, out, 64 * 1024);
                session.fsync(out);
                IoUtil.closeQuietly(in);
                IoUtil.closeQuietly(out);

                session.commit(createIntentSender(context, sessionId, ACTION_INSTALL_COMPLETE));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                IoUtil.closeQuietly(session);
            }
        }
    }

    /**
     * Uninstall.
     *
     * @param context     the context
     * @param packageName the package name
     */
    public static void uninstall(Context context, String packageName) {
        logger.info("uninstall:packageName = {}", packageName);

//        Intent intent = new Intent(context, context.getClass());
//        PendingIntent sender = PendingIntent.getActivity(context, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
            packageInstaller.uninstall(packageName, createIntentSender(context, 0, ACTION_UNINSTALL_COMPLETE));
        }
    }

    private static IntentSender createIntentSender(Context context, int sessionId, String action) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, sessionId, new Intent(action), 0);
        return pendingIntent.getIntentSender();
    }
}
