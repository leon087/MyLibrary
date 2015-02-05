package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import java.io.File;

public class EnvironmentUtil {

    private static final long REMAIN_SPACE = 5 * 1024 * 1024;

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentUtil.class);

    private EnvironmentUtil() {
    }

    /**
     * 取得网络类型
     */
    public static int getNetWorkType(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType();
    }

    /**
     * 判断外部存储已经挂载
     */
    private static boolean isExternalStorageMounted() {
        boolean isMounted = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
        if (!isMounted) {
            logger.info("ExternalStorageState = "
                    + Environment.getExternalStorageState());
        }

        return isMounted;
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(9)
    private static boolean isExternalStorageRemovable() {
        if (SdkUtil.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY
                .equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 判断外部存储是否可用
     */
    public static boolean isExternalStorageWritable() {
        return isExternalStorageMounted();
    }

    /**
     * 获取手机内存项目存储根目录 格式(/data/data/项目包名/files/)
     */
    public static File getDataStorageDirectory(Context context) {
        return context.getFilesDir();
    }

    /**
     * 获取手机sdCard根目录 格式(/sdcard/)
     */
    public static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    @TargetApi(8)
    public static File getExternalStoragePublicDirectory(String type) {
        if (!EnvironmentUtil.SdkUtil.hasFroyo()) {
            File file = new File(EnvironmentUtil.getExternalStorageDirectory(), type);
            file.mkdirs();
            return file;
        }

        File file = Environment.getExternalStoragePublicDirectory(type);
        file.mkdirs();
        return file;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        if (SdkUtil.hasFroyo()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName()
                + "/cache/";
        File externalCacheDir = new File(
                Environment.getExternalStorageDirectory(), cacheDir);
        externalCacheDir.mkdirs();
        return externalCacheDir;
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use
        // external cache dir
        // otherwise use internal cache dir
        final File cachePathFile = isExternalStorageWritable() ? getExternalCacheDir(context)
                : context.getCacheDir();
        File uniqueCacheDir = new File(cachePathFile, uniqueName);
        uniqueCacheDir.mkdirs();
        return uniqueCacheDir;
    }

    /**
     * 获取外部存储目录""/Android/data/"PackageName"/files/""
     */
    @TargetApi(8)
    public static File getExternalFilesDir(Context context, String uniqueName) {
        if (SdkUtil.hasFroyo()) {
            File file = context.getExternalFilesDir(uniqueName);
            if (file != null) {
                return file;
            }
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String filesDir = "/Android/data/" + context.getPackageName()
                + "/files/";
        File externalFilesDir = new File(
                Environment.getExternalStorageDirectory(), filesDir);

        if (uniqueName == null) {
            externalFilesDir.mkdirs();
            return externalFilesDir;
        }

        File dir = new File(externalFilesDir, uniqueName);
        dir.mkdirs();
        // IoUtil.createDirector(dir.getAbsolutePath());
        return dir;
    }

    public static File getExternalDir(Context context) {
        // Before Froyo we need to construct the external cache dir ourselves
        final String filesDir = "/Android/data/" + context.getPackageName();
        File externalFilesDir = new File(
                Environment.getExternalStorageDirectory(), filesDir);
        return externalFilesDir;
    }

    /**
     * 判断是否有足够的空间
     */
    public static boolean hasEnoughSpace(File file) {
        if (getUsableSpace(file) > REMAIN_SPACE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    @TargetApi(9)
    public static long getUsableSpace(File path) {
        if (SdkUtil.hasGingerbread()) {
            return path.getUsableSpace();
        }
        // final StatFs stats = new StatFs(path.getPath());
        // return (long) stats.getBlockSize() * (long)
        // stats.getAvailableBlocks();
        return getAvailableSize(path);
    }

    /**
     * 获取存储可用内存大小
     */
    private static long getAvailableSize(File path) {
        if (path == null || !path.exists()) {
            return -1;
        }
        try {
            StatFs statFs = new StatFs(path.getPath());
            long availableBlocks = statFs.getAvailableBlocks();// 可用存储块的数量
            long blockSize = statFs.getBlockSize();// 每块存储块的大小
            long availableSize = availableBlocks * blockSize;// 可用容量
            return availableSize;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取存储总内存大小
     */
    public static long getTotalSize(String root) {
        if (root == null || "".equals(root)) {
            return -1;
        }

        try {
            StatFs statFs = new StatFs(root);
            long blockCount = statFs.getBlockCount();// 总存储块的数量
            long blockSize = statFs.getBlockSize();// 每块存储块的大小
            long totalSize = blockCount * blockSize;// 总存储量
            return totalSize;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取已用存储大小
     */
    public static long getUsedSize(String root) {
        long size = getTotalSize(root) - getUsableSpace(new File(root));
        if (size >= 0) {
            return size;
        }
        return 0;
    }

    // /**
    // * Returns the default link's IP addresses, if any, taking into account
    // IPv4
    // * and IPv6 style addresses.
    // *
    // * @param context
    // * the application context
    // * @return the formatted and comma-separated IP addresses, or null if
    // none.
    // */
    // public static String getDefaultIpAddresses(Context context) {
    // ConnectivityManager cm = (ConnectivityManager) context
    // .getSystemService(Context.CONNECTIVITY_SERVICE);
    // LinkProperties prop = cm.getActiveLinkProperties();
    // return formatIpAddresses(prop);
    // }
    //
    // private static String formatIpAddresses(LinkProperties prop) {
    // if (prop == null)
    // return null;
    // Iterator<InetAddress> iter = prop.getAddresses().iterator();
    // // If there are no entries, return null
    // if (!iter.hasNext())
    // return null;
    // // Concatenate all available addresses, comma separated
    // String addresses = "";
    // while (iter.hasNext()) {
    // addresses += iter.next().getHostAddress();
    // if (iter.hasNext())
    // addresses += ", ";
    // }
    // return addresses;
    // }

    // @TargetApi(11)
    // public static void enableStrictMode() {
    // if (Utils.hasGingerbread()) {
    // StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
    // new StrictMode.ThreadPolicy.Builder()
    // .detectAll()
    // .penaltyLog();
    // StrictMode.VmPolicy.Builder vmPolicyBuilder =
    // new StrictMode.VmPolicy.Builder()
    // .detectAll()
    // .penaltyLog();
    //
    // if (Utils.hasHoneycomb()) {
    // threadPolicyBuilder.penaltyFlashScreen();
    // vmPolicyBuilder
    // .setClassInstanceLimit(ImageGridActivity.class, 1)
    // .setClassInstanceLimit(ImageDetailActivity.class, 1);
    // }
    // StrictMode.setThreadPolicy(threadPolicyBuilder.build());
    // StrictMode.setVmPolicy(vmPolicyBuilder.build());
    // }
    // }

    /**
     * 判断是否是平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean hasExternalStoragePermission(Context context) {
        return hasPermission(context, permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean hasPermission(Context context, String permission) {
        int perm = context.checkCallingOrSelfPermission(permission);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断SDK版本
     */
    public static class SdkUtil {

        /**
         * 判断手机系统版本是否为{@link android.os.Build.VERSION_CODES#FROYO}以上
         */
        public static boolean hasFroyo() {
            // Can use static final constants like FROYO, declared in later
            // versions
            // of the OS since they are inlined at compile time. This is
            // guaranteed
            // behavior.
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
        }

        /**
         * 判断手机系统版本是否为{@link android.os.Build.VERSION_CODES#GINGERBREAD}以上
         */
        public static boolean hasGingerbread() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
        }

        /**
         * 判断手机系统版本是否为{@link android.os.Build.VERSION_CODES#HONEYCOMB}以上
         */
        public static boolean hasHoneycomb() {
            return Build.VERSION.SDK_INT >= 11;
            // return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        }

        /**
         * 判断手机系统版本是否为{@link android.os.Build.VERSION_CODES#HONEYCOMB_MR1}以上
         */
        public static boolean hasHoneycombMR1() {
            return Build.VERSION.SDK_INT >= 12;
            // return Build.VERSION.SDK_INT >=
            // Build.VERSION_CODES.HONEYCOMB_MR1;
        }

        public static boolean hasJellyBean() {
            return Build.VERSION.SDK_INT >= 16;
            // return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        }

        public static boolean hasJellyBeanMr1() {
            return Build.VERSION.SDK_INT >= 17;
//            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
        }

        public static boolean hasKitkat() {
            return Build.VERSION.SDK_INT >= 19;
//            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        }
    }
}
