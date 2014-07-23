package cm.android.framework.core.manager;

import android.content.Context;
import cm.android.framework.core.global.GlobalData;

public class CommonBaseManager extends BaseManager {

    @Override
    protected void onCreate(Context context) {
        addService(new GlobalData());
    }

    @Override
    protected void onDestroy() {
        ServiceHolder.getService(GlobalData.class).release();
    }
}
