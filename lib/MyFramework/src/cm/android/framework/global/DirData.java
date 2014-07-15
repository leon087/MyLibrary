package cm.android.framework.global;

import android.content.Context;
import cm.android.util.EnvironmentInfo;
import cm.android.util.IoUtil;
import cm.android.util.MyLog;
import cm.android.util.ObjectUtil;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 目录管理基类
 */
public class DirData {
    public static final String WORK_PATH = "workpath";
    private static final HashSet<String> dirsStr = ObjectUtil.newHashSet();
    private final HashMap<String, File> mDirs = ObjectUtil.newHashMap();
    private File workDir = null;

    public DirData(Context context) {
        initWorkDir(context);
    }

    /**
     * 设置初始化目录
     *
     * @param dirName
     */
    public static void initDirName(String... dirName) {
        for (String string : dirName) {
            dirsStr.add(string);
        }
    }

    /**
     * 初始化存储路径
     */
    public void initWorkDir(Context context) {
        workDir = getRootDir(context);
        MyLog.i("workDir = " + workDir.getAbsolutePath());
        initDirs(context);
    }

    // /**
    // * 创建本地存储文件路径
    // */
    // private void createFolder(Context context) {
    // // Utils.createFolder(workPath);
    // for (String dir : mDirs.keySet()) {
    // // IoUtil.createDirector(mDirs.get(dir));
    // EnvironmentInfo.getExternalFilesDir(context, mDirs.get(dir));
    // }
    // }

    private File getRootDir(Context context) {
        File rootDir = new File(context.getFilesDir(), WORK_PATH);
        if (EnvironmentInfo.isExternalStorageUsable()) {
            // 删除files目录下文件
            IoUtil.deleteFiles(rootDir);
            // rootPath = EnvironmentInfo.getExternalStorageDirectory();
            rootDir = EnvironmentInfo.getExternalFilesDir(context, WORK_PATH);
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

    private void initDirs(Context context) {
        for (String dir : dirsStr) {
            File file = new File(workDir, dir);
            file.mkdirs();
            mDirs.put(dir, file);
        }
    }
}
