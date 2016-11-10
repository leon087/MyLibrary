package cm.android.framework.core;

import android.content.Context;
import android.content.Intent;

import cm.java.proguard.annotations.Keep;

@Keep
@Deprecated
public interface IServiceManager {

    void onCreate(Context context);

    void onDestroy();

    void onHandleIntent(Intent intent, int flags, int startId);
}
