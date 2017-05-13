package cm.android.app.test;

import android.os.Bundle;

import cm.android.framework.client.core.LogUtil;
import cm.android.framework.component.BaseContentProvider;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ServiceProvider extends BaseContentProvider {
    public static String AUTHORITIES = "service";

    public static void authorities(String authorities) {
        AUTHORITIES = authorities;
    }

    @Override
    public boolean onCreate() {
        LogUtil.getLogger().error("ggggg onCreate");
        return super.onCreate();
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        LogUtil.getLogger().error("ggggg call:method = {}", method);
        return super.call(method, arg, extras);
    }
}
