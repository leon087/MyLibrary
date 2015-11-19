package cm.android.app.dm;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;

import cm.android.app.core.MyManager;
import cm.android.util.IntentUtil;

public class DefaultDeviceAdminActive implements PolicyManagerImpl.IDeviceAdminActive {

    public static final String ACTION_ACTIVE_DEVICE_ADMIN = "cm.intent.action.ACTIVE_DEVICE_ADMIN";

    public static void active(Activity activity) {
        Intent intent = getAdminIntent(activity);
        activity.startActivity(intent);
//        activity.startActivityForResult(intent, InitActivity.REQUEST_ACTIVE_ADMIN);
    }

    public static Intent getAdminIntent(Activity activity) {
        //启动设备管理（隐式Intent）-在manifest中设定相应过滤器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName componentName = getAdminComponent();
        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

        //描述
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "MDM Control");
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_HISTORY);
        return intent;
    }

    public static ComponentName getAdminComponent() {
        ComponentName componentName = new ComponentName(MyManager.getAppContext(),
                AdminReceiver.class);
        return componentName;
    }

    @Override
    public void active() {
        //发送激活action
        Intent intent = new Intent(ACTION_ACTIVE_DEVICE_ADMIN);
        IntentUtil.sendBroadcastLocal(MyManager.getAppContext(), intent);
    }
}
