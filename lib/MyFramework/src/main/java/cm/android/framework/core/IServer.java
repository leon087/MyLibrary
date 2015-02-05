package cm.android.framework.core;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public interface IServer {

    public IBinder onBind(Intent intent);

    public void onCreate(Context context);

    public void onDestroy();

    public boolean checkAction(String action);

//    public void onStartService(Intent intent, int flags, int startId);

    public void onHandleIntent(Intent intent);
}
