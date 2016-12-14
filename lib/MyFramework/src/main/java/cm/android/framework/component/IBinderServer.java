package cm.android.framework.component;

import android.content.Context;

import cm.java.proguard.annotations.Keep;

public interface IBinderServer {

    void onCreate(Context context);

    void onDestroy();

    boolean isActive(Context context);
}
