package cm.android.framework.core.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.content.Context;

import java.io.File;
import java.util.Map;

import cm.android.util.AndroidUtils;
import cm.android.util.EnvironmentUtil;
import cm.java.util.IoUtil;

/**
 * 目录管理基类
 */
public class WorkDir {

    private static final ExternalWorkDir externalWorkDir = new ExternalWorkDir();

    private static final DataWorkDir dataWorkDir = new DataWorkDir();

    private static final PublicWorkDir publicWorkDir = new PublicWorkDir();

    private static final CustomWorkDir customWorkDir = new CustomWorkDir();

    private static final Logger logger = LoggerFactory.getLogger("WorkDir");

    private WorkDir() {
    }

    public static void initWorkDir(Context context) {
        externalWorkDir.initWorkDir(context, null);
        dataWorkDir.initWorkDir(context, null);
        publicWorkDir.initWorkDir(context, null);
    }

    public static void initCustomWorkDir(Context context, File rootDir) {
        customWorkDir.initWorkDir(context, rootDir);
    }

    public static File getDir(String dir) {
        return externalWorkDir.getDir(dir);
    }

    public static File getDataDir(String dir) {
        return dataWorkDir.getDir(dir);
    }

    public static File getPublicDir(String dir) {
        return publicWorkDir.getDir(dir);
    }

    public static File getCustomDir(String dir) {
        return customWorkDir.getDir(dir);
    }

    public static File[] getWorkDir() {
        File externalDir = externalWorkDir.getWorkDir();
        File dataDir = dataWorkDir.getWorkDir();
        File publicDir = publicWorkDir.getWorkDir();
        File customDir = customWorkDir.getWorkDir();
        return new File[]{externalDir, dataDir, publicDir, customDir};
    }

    private static abstract class BaseWorkDir {

        public static final String WORK_PATH = "workpath";

        private final Map<String, File> dirs = AndroidUtils.newMap();

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
                file = getFile(tag);
                dirs.put(tag, file);
            }
            IoUtil.checkDirectory(file);
            return file;
        }

        private File getFile(String tag) {
            if (workDir == null) {
                workDir = EnvironmentUtil.getExternalStoragePublicDirectory(WORK_PATH);
            }

            File file = new File(workDir, tag);
            return file;
        }

//        public void bindDir(String tag, File dir) {
//            if (Utils.isEmpty(tag)) {
//                tag = dir.getName();
//            }
//            dirs.put(tag, dir);
//        }

//        public Collection<File> getDirs() {
//            return dirs.values();
//        }
    }

    /**
     * sdcard下私有目录
     */
    private static class ExternalWorkDir extends BaseWorkDir {

        @Override
        protected File getRootDir(Context context, File rootDir) {
            File tmpRootDir = EnvironmentUtil.getExternalFilesDir(context, WORK_PATH);
            if (!EnvironmentUtil.isExternalStorageWritable()) {
                logger.error("PrivateExternalWorkDir:isExternalStorageWritable = false");
            }
            return tmpRootDir;
        }
    }

    /**
     * data下私有目录
     */
    private static class DataWorkDir extends BaseWorkDir {

        @Override
        protected File getRootDir(Context context, File rootDir) {
            File tmpRootDir = new File(context.getFilesDir(), WORK_PATH);
            return tmpRootDir;
        }
    }

    private static class PublicWorkDir extends BaseWorkDir {

        @Override
        @TargetApi(8)
        protected File getRootDir(Context context, File rootDir) {
            File file = EnvironmentUtil.getExternalStoragePublicDirectory(context.getPackageName());
            return file;
        }
    }

    private static class CustomWorkDir extends BaseWorkDir {

        @Override
        @TargetApi(8)
        protected File getRootDir(Context context, File rootDir) {
            File file = new File(rootDir, context.getPackageName());
            if (!file.isDirectory() && !file.mkdirs()) {
                logger.error("file = " + file.getAbsolutePath());
                file = getRootDir2(rootDir, context.getPackageName() + "-2");
            }

            return file;
        }

        private File getRootDir2(File rootDir, String uniqueName) {
            File file = new File(rootDir, uniqueName);
            if (!file.isDirectory() && !file.mkdirs()) {
                logger.error("file = " + file.getAbsolutePath());
            }

            return file;
        }
    }

}
