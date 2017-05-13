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
import android.os.storage.StorageManager;

import java.io.File;
import java.util.List;

import cm.java.util.IoUtil;
import cm.java.util.ObjectUtil;
import cm.java.util.Reflect;
import cm.java.util.Utils;

public class EnvironmentUtil {

    private static final long REMAIN_SPACE = 5 * 1024 * 1024;

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentUtil.class);

    private EnvironmentUtil() {
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
    public static boolean isExternalStorageRemovable() {
        if (SdkUtil.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
//        String state = EnvironmentCompat.getStorageState();
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
     * 获取手机sdCard根目录 格式(/sdcard/)
     */
    public static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    @TargetApi(8)
    public static File getExternalStoragePublicDirectory(String type) {
        if (!EnvironmentUtil.SdkUtil.hasFroyo()) {
            File file = new File(EnvironmentUtil.getExternalStorageDirectory(), type);
            IoUtil.checkDirectory(file);
            return file;
        }

        File file = Environment.getExternalStoragePublicDirectory(type);
        IoUtil.checkDirectory(file);
        return file;
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                return file;
            }
        }

        File externalCacheDir = new File(getExternalDir(context), "cache/");
        IoUtil.checkDirectory(externalCacheDir);
        return externalCacheDir;
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use
        // external cache dir
        // otherwise use internal cache dir
        final File cachePathFile = isExternalStorageWritable() ? getExternalCacheDir(context)
                : context.getCacheDir();

        File uniqueCacheDir = cachePathFile;
        if (uniqueName != null) {
            uniqueCacheDir = new File(cachePathFile, uniqueName);
        }
        IoUtil.checkDirectory(uniqueCacheDir);
        return uniqueCacheDir;
    }

    /**
     * 获取外部存储目录""/Android/data/"PackageName"/files/""
     */
    public static File getExternalFilesDir(Context context, String uniqueName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            File file = context.getExternalFilesDir(uniqueName);
            if (file != null) {
                return file;
            }
        }

        File externalFilesDir = new File(getExternalDir(context), "files/");

        if (!Utils.isEmpty(uniqueName)) {
            externalFilesDir = new File(externalFilesDir, uniqueName);
        }
        IoUtil.checkDirectory(externalFilesDir);
        return externalFilesDir;
    }

    public static File getExternalDir(Context context) {
        final String filesDir = "Android/data/" + context.getPackageName();
        File externalFilesDir = new File(Environment.getExternalStorageDirectory(), filesDir);

        IoUtil.checkDirectory(externalFilesDir);
        return externalFilesDir;
    }

    /**
     * 判断是否有足够的空间
     */
    public static boolean hasEnoughSpace(File file) {
        long usable = getUsableSpace(file);
        if (usable > REMAIN_SPACE) {
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

    public static long getTotalSpace(File file) {
        if (SdkUtil.hasGingerbread()) {
            return file.getTotalSpace();
        }

        return getTotalSize(file.getAbsolutePath());
    }

    /**
     * 获取存储总内存大小
     */
    private static long getTotalSize(String root) {
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
    public static long getUsedSize(File root) {
        long size = getTotalSpace(root) - getUsableSpace(root);
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

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public static boolean isSupportInternalSdcard(Context context) {
//        String[] paths = getVolumePaths(context);
//        if (SdkUtil.hasLollipop()) {
//            File internalSdcardFile = new File(paths[0]);
//            boolean removable = Environment.isExternalStorageRemovable(internalSdcardFile);
//            String state = Environment.getExternalStorageState(internalSdcardFile);
//            if (Environment.MEDIA_MOUNTED.equals(state) && !removable) {
//                return true;
//            }
//        }
//        return !EnvironmentUtil.isExternalStorageRemovable();
//    }

    public static String[] getVolumePaths(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            return Reflect.bind(storageManager).call("getVolumePaths");
        } catch (Reflect.ReflectException e) {
            return null;
        }

//        ObjectProxy proxy = new ObjectProxy(storageManager);
//        Method method = proxy.getMethod("getVolumePaths");
//        String[] paths = proxy.doMethod(method);
//        return paths;
    }

    /**
     * 获取外置sdcard目录
     */
    public static List<File> getExtSdcardDirectory(Context context) {
        List<File> fileList = ObjectUtil.newArrayList();

        File file = getExternalStorageDirectory();
        if (EnvironmentUtil.isExternalStorageRemovable()) {
            fileList.add(file);
        }

        String[] array = getVolumePaths(context);
        for (String dir : array) {
            if (file.getAbsolutePath().equals(dir)) {
                continue;
            }

            File extSdcard = new File(dir);
//            if (SdkUtil.hasKitkat()) {
//                String state = Environment.getStorageState(extSdcard);
//                if (Environment.MEDIA_MOUNTED.equals(state)) {
//                    fileList.add(extSdcard);
//                }
//            } else {
            //判断该目录是否可写
            long usable = getUsableSpace(extSdcard);
            if (usable > 0) {
                fileList.add(extSdcard);
//                }
            }
        }

        return fileList;
    }

    /**
     * 判断SDK版本
     */
    public static class SdkUtil {

        public static boolean has(int versionCode) {
            return Build.VERSION.SDK_INT >= versionCode;
        }

        /**
         * 判断手机系统版本是否为{@link android.os.Build.VERSION_CODES#FROYO}以上
         */
        public static boolean hasFroyo() {
            // Can use static final constants like FROYO, declared in later
            // versions
            // of the OS since they are inlined at compile time. This is
            // guaranteed
            // behavior.
            return has(Build.VERSION_CODES.FROYO);
        }

        /**
         * 判断手机系统版本是否为{@link android.os.Build.VERSION_CODES#GINGERBREAD}以上
         */
        public static boolean hasGingerbread() {
            return has(Build.VERSION_CODES.GINGERBREAD);
        }

        /**
         * 判断手机系统版本是否为{@link android.os.Build.VERSION_CODES#HONEYCOMB}以上
         */
        public static boolean hasHoneycomb() {
            return has(11);
            // return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        }

        /**
         * 判断手机系统版本是否为{@link android.os.Build.VERSION_CODES#HONEYCOMB_MR1}以上
         */
        public static boolean hasHoneycombMR1() {
            return has(12);
            // return Build.VERSION.SDK_INT >=
            // Build.VERSION_CODES.HONEYCOMB_MR1;
        }

        public static boolean hasJellyBean() {
            return has(16);
            // return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        }

        public static boolean hasJellyBeanMr1() {
            return has(17);
//            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
        }

        public static boolean hasJellyBeanMr2() {
            return has(18);
//            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
        }

        public static boolean hasKitkat() {
            return has(19);
//            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        }

        public static boolean hasLollipop() {
            return has(21);
        }
    }
}
