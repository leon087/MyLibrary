package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO读写Util类
 */
public class AndroidIoUtil {

    private static final Logger logger = LoggerFactory.getLogger(AndroidIoUtil.class);

    private AndroidIoUtil() {
    }

    /**
     * 判断目录是否可用, 已经挂载并且拥有可读可写权限 true 可用
     */
    public static boolean isDirectoryValid(String path) {
        File file = new File(path);
        if (!file.canWrite()) {
            return false;
        }
        StatFs sf = new StatFs(file.getPath());
        long availCount = sf.getAvailableBlocks();
        if (availCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * 移动资源配置文件至制定路径
     */
    public static void copyRes(Context context, File destFile, int res) {
        if (destFile.exists()) {
            return;
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            cm.java.util.IoUtil.createFile(destFile);

            inputStream = new BufferedInputStream(context.getResources()
                    .openRawResource(res));
            outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
            cm.java.util.IoUtil.write(inputStream, outputStream);
        } catch (IOException e) {
            logger.error("destFile = " + destFile, e);
        } finally {
            cm.java.util.IoUtil.closeQuietly(inputStream);
            cm.java.util.IoUtil.closeQuietly(outputStream);
        }
    }

    /**
     * 移动APK文件至制定路径
     */
    public static void writeApkToPhone(Context context, String fileName, int res) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (cm.java.util.IoUtil.isFileExist(fileName)) {
                return;
            }
            cm.java.util.IoUtil.createFile(new File(fileName));

            inputStream = new BufferedInputStream(context.getResources()
                    .openRawResource(res));
            outputStream = new BufferedOutputStream(context.openFileOutput(
                    fileName, Context.MODE_WORLD_READABLE
                            | Context.MODE_WORLD_WRITEABLE));
            cm.java.util.IoUtil.write(inputStream, outputStream);
        } catch (IOException e) {
            logger.error("fileName = " + fileName, e);
        } finally {
            cm.java.util.IoUtil.closeQuietly(inputStream);
            cm.java.util.IoUtil.closeQuietly(outputStream);
        }
    }

    /**
     * 移动Assets目录下的文件
     */
    public static void copyAssetFiles(Context context, String assetPath, File dir) {
        if (cm.java.util.IoUtil.getFiles(dir).length != 0) {
            return;
        }

        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(assetPath);
        } catch (IOException e) {
            logger.error("assetPath = " + assetPath, e);
            return;
        }

        for (String strSvy : files) {
            String filePath = assetPath + File.separator + strSvy;
            copyAssetFile(assetManager, filePath, new File(dir, strSvy));
        }
    }

    public static boolean copyAssetFile(AssetManager assetManager, String fileName, File destFile) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(assetManager.open(fileName));
            outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
            cm.java.util.IoUtil.write(inputStream, outputStream);
            return true;
        } catch (IOException e) {
            logger.error("fileName = {},destFile = {},e = {}", fileName, destFile, e);
            return false;
        } finally {
            cm.java.util.IoUtil.closeQuietly(inputStream);
            cm.java.util.IoUtil.closeQuietly(outputStream);
        }
    }

    /**
     * 拷贝用户目录下文件到指定路径
     *
     * @param srcName The name of the file to open; can not contain path separators.
     */
    public static void copyPrivateFile(Context context, String srcName,
            File destFile) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(
                    context.openFileInput(srcName));
            outputStream = new BufferedOutputStream(new FileOutputStream(
                    destFile));
            cm.java.util.IoUtil.write(inputStream, outputStream);
        } catch (IOException e) {
            logger.error("srcName = " + srcName + ",destFile = " + destFile.getAbsolutePath(),
                    e);
        } finally {
            cm.java.util.IoUtil.closeQuietly(inputStream);
            cm.java.util.IoUtil.closeQuietly(outputStream);
        }
    }

    /**
     * 把srcFilePath写入/data/data/<包名>/files/目录下
     */
    public static boolean writeToDataDir(String srcFilePath,
            String destFileName, Context context) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(
                    srcFilePath));
            outputStream = new BufferedOutputStream(context.openFileOutput(
                    destFileName, Context.MODE_WORLD_READABLE
                            | Context.MODE_WORLD_WRITEABLE));
            cm.java.util.IoUtil.write(inputStream, outputStream);
            return true;
        } catch (Exception e) {
            logger.error("srcFilePath = " + srcFilePath + ",destFileName = "
                    + destFileName, e);
            return false;
        } finally {
            cm.java.util.IoUtil.closeQuietly(inputStream);
            cm.java.util.IoUtil.closeQuietly(outputStream);
        }
    }
}