package cm.android.util;

import android.content.Context;

import java.io.File;

/**
 * 主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 */
public class UserDataCleaner {

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    public static void cleanCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(
                new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    public static void cleanExternalCache(Context context) {
        if (EnvironmentUtil.isExternalStorageWritable()) {
            deleteFilesByDirectory(EnvironmentUtil.getExternalCacheDir(context));
        }
    }

    public static void cleanExternalFiles(Context context) {
        if (EnvironmentUtil.isExternalStorageWritable()) {
            deleteFilesByDirectory(EnvironmentUtil.getExternalFilesDir(context, null));
        }
    }

    public static void cleanExternalDir(Context context) {
        if (EnvironmentUtil.isExternalStorageWritable()) {
            deleteFilesByDirectory(EnvironmentUtil.getExternalDir(context));
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    public static void cleanCustomFile(File file) {
        deleteFilesByDirectory(file);
    }

    /**
     * 清除本应用所有的数据
     */
    public static void cleanUserData(Context context, File... files) {
        cleanCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);

        cleanExternalCache(context);
        cleanExternalFiles(context);
        cleanExternalDir(context);

        for (File file : files) {
            cleanCustomFile(file);
        }
    }

    /**
     * 删除方法
     */
    private static void deleteFilesByDirectory(File directory) {
        IoUtil.deleteDir(directory);
//        IoUtil.deleteFiles(directory);
    }

}
