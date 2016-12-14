package cm.android.framework.ext.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cm.java.util.IoUtil;

public class Command {
    private static final Logger logger = LoggerFactory.getLogger("Command");

    /** copy file to destination */
    private static void copyFile(File file, InputStream is, String mode) throws IOException, InterruptedException {
        FileOutputStream out = null;
        try {
            final String abspath = file.getAbsolutePath();
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024 * 8];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            Runtime.getRuntime().exec("chmod " + mode + " " + abspath).waitFor();
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        } finally {
            IoUtil.closeQuietly(out);
        }
    }

    /**
     * copy file in assets into destination file
     *
     * @param context        context
     * @param assetsFilename file name in assets
     * @param file           the file to copy to
     * @param mode           mode of file
     */
    public static void copyAssets(Context context, String assetsFilename, File file, String mode)
            throws IOException, InterruptedException {
        AssetManager manager = context.getAssets();
        final InputStream is = manager.open(assetsFilename);
        try {
            copyFile(file, is, mode);
        } finally {
            IoUtil.closeQuietly(is);
        }
    }

    /**
     * Install specified binary into destination directory.
     *
     * @param context  context
     * @param destDir  destionation directory
     * @param filename filename of binary
     * @return true if install successfully, otherwise return false
     */
    @SuppressWarnings("deprecation")
    public static boolean install(Context context, String destDir, String filename) {
        String abi = Build.CPU_ABI;
        if (!abi.startsWith("arm")) {
            return false;
        }
        try {
            File f = new File(context.getDir(destDir, Context.MODE_PRIVATE), filename);
            if (f.exists()) {
                logger.info("binary has existed");
                return false;
            }
            copyAssets(context, filename, f, "0755");
            return true;
        } catch (Exception e) {
            logger.error("installBinary failed: " + e.getMessage());
            return false;
        }
    }
}
