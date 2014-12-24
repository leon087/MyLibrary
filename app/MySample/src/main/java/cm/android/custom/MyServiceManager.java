package cm.android.custom;

import android.content.Context;

import cm.android.common.http.HttpLoader;
import cm.android.common.http.MyHttp;
import cm.android.framework.core.manager.CommonBaseManager;
import cm.android.framework.core.manager.ServiceHolder;
import cm.android.sdk.content.ExternalStorageReceiver;
import cm.android.sdk.content.ExternalStorageReceiver.ExternalStorageListener;

public class MyServiceManager extends CommonBaseManager {

    private Context mContext;

    MyServiceManager() {
    }

    private ExternalStorageListener externalStorageListener = new ExternalStorageListener() {

        @Override
        public void onMediaMounted() {
            //WorkDir.initWorkDir(mContext);
        }

        @Override
        public void onMediaRemoved() {
            //WorkDir.initWorkDir(mContext);
        }
    };

    @Override
    protected void onInit(Context context) {
        super.onInit(context);
        mContext = context;
        addAppService(new MyHttp(mContext));
    }

    @Override
    protected void onCreate() {
        // addService(new MyDaoManager().init(context));
        super.onCreate();

        HttpLoader.init(mContext);

        addService(new ExternalStorageReceiver(mContext, externalStorageListener));
    }

    @Override
    protected void onDestroy() {
        ServiceHolder.getService(MyHttp.class).cancel();
        ServiceHolder.getService(ExternalStorageReceiver.class).release();
        super.onDestroy();

        // getService(MyDaoManager.class).deInit();
    }

}
