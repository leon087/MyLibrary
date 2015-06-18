package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import cm.android.sdk.content.BaseBroadcastReceiver;
import cm.android.util.IntentUtil;

public abstract class CoreReceiver extends BaseBroadcastReceiver {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private static final String ACTION = "cm.android.framework.intent.action.CoreReceiver";

    private static final String PROC_NAME = "processName";

    @Override
    public void onReceive(Context context, Intent intent) {
        String processName = intent.getStringExtra(PROC_NAME);
        logger.info("processName = {},intent = {}", processName, intent);
//        if (context.getPackageName().equals(processName)) {
        restore();
//        }
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);

        //守护action
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(Intent.ACTION_USER_PRESENT);

        return filter;
    }

    public static final void restore(Context context, String processName) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra(PROC_NAME, processName);
        IntentUtil.sendBroadcastInternal(context, intent);
    }

    //恢复
    public abstract void restore();
}
