package cm.android.app.core;

import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import cm.android.framework.core.ServiceManager;
import cm.android.sdk.content.BaseBroadcastReceiver;
import cm.android.util.SystemUtil;

public class DaemonReceiver extends BaseBroadcastReceiver {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("core");

    @Override
    public void onReceive(Context context, Intent intent) {
        String processName = SystemUtil.getCurProcessName();
        logger.info("processName = {},intent = {}", processName, intent);
        if (!context.getPackageName().equals(processName)) {
            return;
        }

        ServiceManager.restoreService(context, processName);
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
//        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        filter.addAction(ServiceManager.ACTION_BIND_SUCCEED);
        return filter;
    }
}
