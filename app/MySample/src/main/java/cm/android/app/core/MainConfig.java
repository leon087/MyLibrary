package cm.android.app.core;

import android.content.Context;

import cm.android.framework.client.core.LogUtil;
import cm.android.framework.ext.util.WorkDir;
import cm.android.log.LogManager;
import cm.android.log.timber.FileTree;
import cm.android.log.timber.Level;
import cm.android.log.timber.LogcatTree;
import cm.android.util.AndroidUtils;

public class MainConfig {

    public static final String DOWNLOAD = "download/";

    public static final String LOG_DIR = "log/";

    /**
     * 初始化
     */
    public void init(Context context) {
        initEnvironment(context);
        initWorkDir(context);
        initLog(context);
    }

    private void initEnvironment(Context context) {

    }

    private void initWorkDir(Context context) {
        WorkDir.initWorkDir(context);
    }

    private void initLog(Context context) {
//        LogConfig.configPattern(true);
//        LogConfig.configLogback(Level.ALL, WorkDir.getDir(LOG_DIR));

        FileTree fileTree = new FileTree(WorkDir.getDir(LOG_DIR));
        LogcatTree logcatTree = new LogcatTree();

        boolean debug = AndroidUtils.isDebuggable(context);
        Level level = Level.ERROR;
        if (debug) {
            level = Level.ALL;
        }

        LogManager.setLevel(level);
        LogManager.initTree(fileTree, logcatTree);
        LogUtil.getLogger().error("Hello {}", "ggg");
    }
}
