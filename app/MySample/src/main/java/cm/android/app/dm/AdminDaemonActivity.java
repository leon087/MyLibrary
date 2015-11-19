package cm.android.app.dm;

import android.app.Activity;
import android.content.Intent;

public class AdminDaemonActivity extends Activity {

    public static final int REQUEST_ACTIVE_ADMIN = 100;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAdmin();
    }

    public static void activeAdmin(Activity activity) {
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

    //初始化判断
    private void checkAdmin() {
        if (!PolicyManagerImpl.getInstance(this).isAdminActive()) {
            activeAdmin(this);
        } else {
            finish();
        }
    }

}
