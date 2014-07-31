package cm.android.framework.core;

import android.content.Context;
import cm.android.util.EnvironmentUtil;
import cm.android.util.IoUtil;
import cm.android.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;

/**
 * 目录管理基类
 */
public class WorkDir {
    public static final String WORK_PATH = "workpath";
    private static final HashMap<String, File> mDirs = ObjectUtil.newHashMap();
    private File workDir = null;

    private static final Logger logger = LoggerFactory.getLogger(WorkDir.class);

    private WorkDir() {
    }

    private static final class Singleton {
        private static final WorkDir SINGLETON = new WorkDir();
    }

    public static WorkDir getInstance() {
        return Singleton.SINGLETON;
    }

    /**
     * 初始化存储路径
     */
    public void initWorkDir(Context context, String... dirName) {
        workDir = getRootDir(context);
        logger.info("workDir = " + workDir.getAbsolutePath());
        initDirs(context, dirName);
    }

    private File getRootDir(Context context) {
        File rootDir = new File(context.getFilesDir(), WORK_PATH);
        if (EnvironmentUtil.isExternalStorageUsable()) {
            // 删除files目录下文件
            IoUtil.deleteFiles(rootDir);
            // rootPath = EnvironmentUtil.getExternalStorageDirectory();
            rootDir = EnvironmentUtil.getExternalFilesDir(context, WORK_PATH);
        }
        return rootDir;
    }

    /**
     * 获取目录
     *
     * @param dir
     * @return
     */
    public File getDir(String dir) {
        // return new File(workDir, mDirs.get(dir)).getAbsolutePath()
        // + File.separator;
        return mDirs.get(dir);
    }

    private void initDirs(Context context, String... dirNames) {
        for (String dir : dirNames) {
            File file = new File(workDir, dir);
            file.mkdirs();
            mDirs.put(dir, file);
        }
    }
}
