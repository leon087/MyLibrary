package cm.android.app.core;

import android.content.Context;

import cm.android.framework.client.core.LogUtil;
import cm.android.framework.core.ServiceManager;
import cm.android.framework.ext.util.WorkDir;
import cm.android.log.timber.FileTree;
import cm.android.log.timber.Level;
import cm.android.log.timber.LogcatTree;
import cm.android.util.AndroidUtils;
import timber.log.Timber;

public class MainConfig extends ServiceManager.AppConfig {

    public static final String DOWNLOAD = "download/";

    public static final String LOG_DIR = "log/";

    @Override
    public void initEnvironment(Context context) {

    }

    @Override
    public void initWorkDir(Context context) {
        WorkDir.initWorkDir(context);
    }

    @Override
    public void initLog(Context context) {
//        LogConfig.configPattern(true);
//        LogConfig.configLogback(Level.ALL, WorkDir.getDir(LOG_DIR));

        FileTree fileTree = new FileTree(WorkDir.getDir(LOG_DIR));
        LogcatTree logcatTree = new LogcatTree();

        if (AndroidUtils.isDebuggable(context)) {
            fileTree.setLevel(Level.ALL);
            logcatTree.setLevel(Level.ALL);
        } else {
            fileTree.setLevel(Level.INFO);
            logcatTree.setLevel(Level.INFO);
        }
        Timber.uprootAll();
        Timber.plant(fileTree);
        Timber.plant(logcatTree);
        LogUtil.getLogger().error("Hello {}", "ggg");
    }
}
