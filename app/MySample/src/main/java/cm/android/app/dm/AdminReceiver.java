package cm.android.app.dm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;

import cm.android.framework.core.ServiceManager;

public class AdminReceiver extends DeviceAdminReceiver {

    private static final Logger logger = LoggerFactory.getLogger("DeviceAdmin");

    @Override
    public DevicePolicyManager getManager(Context context) {
        return super.getManager(context);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);

        logger.info("onEnabled intent = " + intent);

        if (!ServiceManager.isStarted()) {
            return;
        }
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        CharSequence reqeustStr = "";
        logger.info("reqeustStr = " + "");
        return reqeustStr;
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);

        logger.info("onDisabled intent = " + intent);

        PolicyManagerImpl.getInstance(context).onDisabled();
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        super.onPasswordChanged(context, intent);
        logger.info("onPasswordChanged intent = " + intent);
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        super.onPasswordFailed(context, intent);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        super.onPasswordSucceeded(context, intent);
    }

    @Override
    public void onPasswordExpiring(Context context, Intent intent) {
        super.onPasswordExpiring(context, intent);
    }

}
