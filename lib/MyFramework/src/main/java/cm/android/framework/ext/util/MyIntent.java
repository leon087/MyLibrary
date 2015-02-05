package cm.android.framework.ext.util;

import android.os.Bundle;

/**
 * 封装了一些Intent操作
 */
public class MyIntent {

    private static final String BUNDLE_INSTANCESTATE = "InstanceState";

    public static void backup(Bundle outState, Bundle bundleBak) {
        outState.putBundle(BUNDLE_INSTANCESTATE, bundleBak);
    }

    public static void restore(Bundle savedInstanceState, Bundle bundleBak) {
        bundleBak.clear();

        if (null == savedInstanceState) {
            return;
        }

        Bundle bundle = savedInstanceState.getBundle(BUNDLE_INSTANCESTATE);
        if (bundle != null) {
            bundleBak.putAll(bundle);
        }
    }
}
