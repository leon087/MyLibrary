package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.content.Context;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import cm.android.util.EnvironmentUtil;
import cm.android.util.ObjectUtil;
import cm.android.util.Utils;

/**
 * 目录管理基类
 */
public class WorkDir {

    private static final PrivateWorkDir privateWorkDir = new PrivateWorkDir();

    private static final PublicWorkDir publicWorkDir = new PublicWorkDir();

    private static final Logger logger = LoggerFactory.getLogger(WorkDir.class);

    private WorkDir() {
    }

    public static void initWorkDir(Context context, String... dirNames) {
        privateWorkDir.initWorkDir(context, dirNames);
    }

    public static void initPublicWorkDir(Context context, String... dirNames) {
        publicWorkDir.initWorkDir(context, dirNames);
    }

    public static File getDir(String dir) {
        return privateWorkDir.getDir(dir);
    }

    public static File getPublicDir(String dir) {
        return publicWorkDir.getDir(dir);
    }

    public static Collection<File> getDirs() {
        return privateWorkDir.getDirs();
    }

    public static Collection<File> getPublicDirs() {
        return publicWorkDir.getDirs();
    }

    public static void bindPublicDir(String tag, File dir) {
        publicWorkDir.bindDir(tag, dir);
    }

    private static abstract class BaseWorkDir {

        private final HashMap<String, File> dirs = ObjectUtil.newHashMap();

        protected File workDir = null;

        protected void initWorkDir(Context context, String... dirNames) {
            workDir = getRootDir(context);
            logger.info("workDir = " + workDir.getAbsolutePath());
            initDirs(context, dirNames);
        }

        protected abstract File getRootDir(Context context);

        /**
         * 获取目录
         */
        public File getDir(String dir) {
            // return new File(workDir, mDirs.get(dir)).getAbsolutePath()
            // + File.separator;
            return dirs.get(dir);
        }

        private void initDirs(Context context, String... dirNames) {
            for (String dir : dirNames) {
                File file = new File(workDir, dir);
                file.mkdirs();
                dirs.put(dir, file);
            }
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
        protected File getRootDir(Context context) {
            File rootDir = new File(context.getFilesDir(), WORK_PATH);
            if (EnvironmentUtil.isExternalStorageWritable()) {
                // 删除files目录下文件
                //IoUtil.deleteFiles(rootDir);
                rootDir = EnvironmentUtil.getExternalFilesDir(context, WORK_PATH);
            }
            return rootDir;
        }

    }

    private static class PublicWorkDir extends BaseWorkDir {

        @Override
        @TargetApi(8)
        protected File getRootDir(Context context) {
            return EnvironmentUtil.getExternalStoragePublicDirectory(context.getPackageName());
        }
    }
}
