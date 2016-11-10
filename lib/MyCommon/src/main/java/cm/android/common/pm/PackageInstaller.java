package cm.android.common.pm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.net.Uri;

/**
 * Created by Administrator on 2016/8/17.
 */
public class PackageInstaller {
    private static final Logger logger = LoggerFactory.getLogger("pm");
//
//    private Context context;
//
//    public PackageInstaller(Context context) {
//    }
//
//    public void instatllBatch(String path, String packageName) {
//
//        logger.info("path=" + path);
//        int installFlags = 0;
//        try {
//            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
//            if (pi != null) {
//                installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
//            }
//        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
//        }
//        if ((installFlags & PackageManager.INSTALL_REPLACE_EXISTING) != 0) {
//            logger.info("Replacing package:" + packageName);
//        }
//
//        // Create temp file before invoking install api
//        mTmpFile = createTempPackageFile(path);
//        if (mTmpFile == null) {
//            // Message msg = mHandler.obtainMessage(INSTALL_COMPLETE);
//            // msg.arg1 = PackageManager.INSTALL_FAILED_INSUFFICIENT_STORAGE;
//            // mHandler.sendMessage(msg);
//            return;
//        }
//        Uri mPackageURI = Uri.parse("file://" + mTmpFile.getPath());
//        String installerPackageName = mContext.getIntent().getStringExtra(
//                Intent.EXTRA_INSTALLER_PACKAGE_NAME);
//
//        PackageInstallObserver observer = new PackageInstallObserver();
//        PackageManager.getInstance().install(context, mPackageURI, observer, installFlags);
//    }
//
//    private File createTempPackageFile(String filePath) {
//        File tmpPackageFile = context.getFileStreamPath(TMP_FILE_NAME);
//        if (tmpPackageFile == null) {
//            Log.w(TAG, "Failed to create temp file");
//            return null;
//        }
//        if (tmpPackageFile.exists()) {
//            tmpPackageFile.delete();
//        }
//        // Open file to make it world readable
//        FileOutputStream fos;
//        try {
//            fos = openFileOutput(TMP_FILE_NAME, MODE_WORLD_READABLE);
//        } catch (FileNotFoundException e1) {
//            Log.e(TAG, "Error opening file " + TMP_FILE_NAME);
//            return null;
//        }
//        try {
//            fos.close();
//        } catch (IOException e) {
//            Log.e(TAG, "Error opening file " + TMP_FILE_NAME);
//            return null;
//        }
//
//        File srcPackageFile = new File(filePath);
//        if (!FileUtils.copyFile(srcPackageFile, tmpPackageFile)) {
//            Log.w(TAG, "Failed to make copy of file: " + srcPackageFile);
//            return null;
//        }
//        return tmpPackageFile;
//    }

    public static void install(Context context, Uri uri, final PackageManager.PackageInstallObserver observer) {
//        int installFlags = 0;
//        try {
//            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
//            if (pi != null) {
//                installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
//            }
//        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
//        }
//        if ((installFlags & PackageManager.INSTALL_REPLACE_EXISTING) != 0) {
//            logger.info("Replacing package:" + packageName);
//        }

        PackageManager.getInstance().installPackage(context, uri, observer, PackageManager.INSTALL_REPLACE_EXISTING);
    }

    public static void uninstall(Context context, String packageName, final PackageManager.PackageDeleteObserver observer) {
        PackageManager.getInstance().deletePackage(context, packageName, observer, 0);
    }
}
