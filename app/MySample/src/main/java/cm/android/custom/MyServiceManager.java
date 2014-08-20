package cm.android.custom;

import android.content.Context;
import cm.android.common.http.HttpLoader;
import cm.android.common.http.HttpUtil;
import cm.android.framework.core.WorkDir;
import cm.android.framework.core.manager.CommonBaseManager;
import cm.android.framework.core.manager.ServiceHolder;
import cm.android.sdk.ExternalStorageReceiver;
import cm.android.sdk.ExternalStorageReceiver.ExternalStorageListener;

public class MyServiceManager extends CommonBaseManager {
    private Context mContext;

    MyServiceManager() {
    }

    private ExternalStorageListener externalStorageListener = new ExternalStorageListener() {

        @Override
        public void onMediaMounted() {
            WorkDir.getInstance().initWorkDir(mContext);
        }

        @Override
        public void onMediaRemoved() {
            WorkDir.getInstance().initWorkDir(mContext);
        }
    };

    @Override
    protected void onCreate(Context context) {
        // addService(new MyDaoManager().init(context));
        super.onCreate(context);

        HttpLoader.init(context);
        mContext = context;

        addService(new ExternalStorageReceiver(context, externalStorageListener));
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancel(mContext);

        ServiceHolder.getService(ExternalStorageReceiver.class).release();
        super.onDestroy();

        // getService(MyDaoManager.class).deInit();
    }

}
