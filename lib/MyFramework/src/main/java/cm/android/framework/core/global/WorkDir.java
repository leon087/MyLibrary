package cm.android.framework.core.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.content.Context;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import cm.android.util.EnvironmentUtil;
import cm.java.util.IoUtil;
import cm.java.util.ObjectUtil;
import cm.java.util.Utils;

/**
 * 目录管理基类
 */
public class WorkDir {

    private static final PrivateWorkDir privateWorkDir = new PrivateWorkDir();

    private static final PublicWorkDir publicWorkDir = new PublicWorkDir();

    private static final Logger logger = LoggerFactory.getLogger("WorkDir");

    private WorkDir() {
    }

    public static void initWorkDir(Context context) {
        privateWorkDir.initWorkDir(context, null);
    }

    public static void initPublicWorkDir(Context context, File rootDir) {
        publicWorkDir.initWorkDir(context, rootDir);
    }

    public static void initPublicWorkDir(Context context) {
        publicWorkDir.initWorkDir(context, null);
    }

    public static File getDir(String dir) {
        return privateWorkDir.getDir(dir);
    }

    public static File getPublicDir(String dir) {
        return publicWorkDir.getDir(dir);
    }

    public static File getWorkDir() {
        return privateWorkDir.getWorkDir();
    }

    public static File getPublicWorkDir() {
        return publicWorkDir.getWorkDir();
    }

    public static void bindPublicDir(String tag, File dir) {
        publicWorkDir.bindDir(tag, dir);
    }

    public static Collection<File> getPublicDirs() {
        return publicWorkDir.getDirs();
    }

    private static abstract class BaseWorkDir {

        private final HashMap<String, File> dirs = ObjectUtil.newHashMap();

        protected File workDir = null;

        protected void initWorkDir(Context context, File rootDir) {
            workDir = getRootDir(context, rootDir);
            logger.info("workDir = " + workDir.getAbsolutePath());
        }

        File getWorkDir() {
            return workDir;
        }

        protected abstract File getRootDir(Context context, File rootDir);

        /**
         * 获取目录
         */
        public File getDir(String tag) {
            File file = dirs.get(tag);
            if (file == null) {
                file = new File(workDir, tag);
                dirs.put(tag, file);
            }
            IoUtil.checkDirectory(file);
            return file;
        }

        public void bindDir(String tag, File dir) {
            if (Utils.isEmpty(tag)) {
                tag = dir.getName();
            }
            dirs.put(tag, dir);
        }

        public Collection<File> getDirs() {
            return dirs.values();
        }
    }

    private static class PrivateWorkDir extends BaseWorkDir {

        public static final String WORK_PATH = "workpath";

        @Override
        protected File getRootDir(Context context, File rootDir) {
            File tmpRootDir = new File(context.getFilesDir(), WORK_PATH);
            if (EnvironmentUtil.isExternalStorageWritable()) {
                // 删除files目录下文件
                //IoUtil.deleteFiles(rootDir);
                tmpRootDir = EnvironmentUtil.getExternalFilesDir(context, WORK_PATH);
            }
            return tmpRootDir;
        }

    }

    private static class PublicWorkDir extends BaseWorkDir {

        @Override
        @TargetApi(8)
        protected File getRootDir(Context context, File rootDir) {
            if (rootDir == null) {
                return EnvironmentUtil.getExternalStoragePublicDirectory(context.getPackageName());
            }

            File file = new File(rootDir, context.getPackageName());
            if (!file.isDirectory() && !file.mkdirs()) {
                return EnvironmentUtil.getExternalStoragePublicDirectory(context.getPackageName());
            }

            return file;
        }
    }
}
