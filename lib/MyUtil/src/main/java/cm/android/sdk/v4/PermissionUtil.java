package cm.android.sdk.v4;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {
    public boolean checkDenied(Context context, String... permissions) {
        for (String permission : permissions) {
            if (denied(context, permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean denied(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }
}
