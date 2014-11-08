package cm.android.sdk.content;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class LocalBroadcastHelper {

    public static void registerReceiver(Context context, BaseBroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, receiver.createIntentFilter());
    }

    public static void sendBroadcast(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendBroadcastSync(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent);
    }

    public static void unregisterReceiver(Context context, BaseBroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }
}
