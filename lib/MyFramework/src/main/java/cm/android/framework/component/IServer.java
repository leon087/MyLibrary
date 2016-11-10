package cm.android.framework.component;

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

    public static final IServer EMPTY_SERVER = new IServer() {
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate(Context context) {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public boolean checkAction(String action) {
            return false;
        }

        @Override
        public void onHandleIntent(Intent intent) {
        }
    };
}
