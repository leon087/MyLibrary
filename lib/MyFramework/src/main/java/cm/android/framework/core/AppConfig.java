package cm.android.framework.core;

import android.content.Context;

/**
 */
public abstract class AppConfig implements IAppConfig {

    /**
     * 初始化
     */
    public void init(Context context) {
        initWorkDir(context);
        initLog();
    }
}
