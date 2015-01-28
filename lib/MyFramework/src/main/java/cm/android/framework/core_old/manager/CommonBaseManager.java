package cm.android.framework.core_old.manager;

import android.content.Context;

import cm.android.framework.core.global.GlobalData;

public class CommonBaseManager extends BaseManager {

    @Override
    protected void onInit(Context context) {

    }

    @Override
    protected void onCreate() {
        GlobalData.getInstance().reset();
    }

    @Override
    protected void onDestroy() {
        GlobalData.getInstance().reset();
    }
}
