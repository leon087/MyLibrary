package cm.android.framework.server;

import android.content.Context;

import cm.java.proguard.annotations.Keep;

@Keep
public interface IBinderServer {

    void onCreate(Context context);

    void onDestroy();

    boolean isActive(Context context);
}
