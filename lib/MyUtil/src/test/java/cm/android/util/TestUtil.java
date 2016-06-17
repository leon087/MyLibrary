package cm.android.util;

import org.robolectric.RuntimeEnvironment;

import android.content.Context;

public class TestUtil {
    public static Context getContext() {
        return RuntimeEnvironment.application;
    }
}
