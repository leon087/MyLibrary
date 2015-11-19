package cm.android.app.dm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import cm.android.sdk.content.BaseBroadcastReceiver;

public class DmActivity extends Activity {

    public static final int REQUEST_ACTIVE_ADMIN = 100;

    @Override
    protected void onPause() {
        super.onPause();
        deInitAdminPolicy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdminPolicy();

        Log.e("ggg", "ggg onResume");
        checkAdmin();
    }

    private void initAdminPolicy() {
        LocalBroadcastManager.getInstance(this).registerReceiver(adminPolicyReceiver,
                adminPolicyReceiver.createIntentFilter());

//        activeAdmin();
    }

    private void deInitAdminPolicy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(adminPolicyReceiver);
    }

    public static void activeAdmin(Activity activity) {
        //CMCC
        Intent intent = DefaultDeviceAdminActive.getAdminIntent(activity);
        activity.startActivityForResult(intent, REQUEST_ACTIVE_ADMIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_ACTIVE_ADMIN) {
//            checkAdmin();
//        }
    }

    private BaseBroadcastReceiver adminPolicyReceiver = new BaseBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DefaultDeviceAdminActive.ACTION_ACTIVE_DEVICE_ADMIN.equals(action)) {
                activeAdmin(DmActivity.this);
            }
        }

        public android.content.IntentFilter createIntentFilter() {
            IntentFilter filter = new IntentFilter();
            //设备管理器
            filter.addAction(DefaultDeviceAdminActive.ACTION_ACTIVE_DEVICE_ADMIN);
            return filter;
        }
    };

    //初始化判断
    private void checkAdmin() {
        if (!PolicyManagerImpl.getInstance(this).isAdminActive()) {
            activeAdmin(this);
        } else {
            finish();
        }
    }

}
