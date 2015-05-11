package cm.android.app.core;

import android.content.Context;

import ch.qos.logback.classic.Level;
import cm.android.framework.core.ServiceManager;
import cm.android.framework.core.global.WorkDir;
import cm.android.log.LogConfig;

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
        LogConfig.configLogback(Level.ALL, WorkDir.getDir(LOG_DIR));
    }
}
