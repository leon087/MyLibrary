package cm.android.framework.core;

import android.content.Context;

import cm.java.proguard.annotations.Keep;

@Keep
public interface IServiceManager {

    void onCreate(Context context);

    void onDestroy();
}
