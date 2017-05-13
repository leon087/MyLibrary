package cm.android.framework.component;

import android.content.Context;

public interface IBinderServer {

    void onCreate(Context context);

    void onDestroy();

    boolean isActive(Context context);
}
