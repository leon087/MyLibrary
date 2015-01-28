package cm.android.custom;

import android.content.Context;

import cm.android.common.http.MyHttp;
import cm.android.framework.core.AppConfig;
import cm.android.framework.core.BaseApp;
import cm.android.framework.core.ServiceManager;
import cm.android.framework.core.manager.IServiceManager;

public class MainApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AppConfig initConfig() {
        return new MainConfig();
    }

    @Override
    protected IServiceManager initService() {
        return new IServiceManager() {
            @Override
            public void onCreate() {
                ServiceManager.addService("Mdmhttp", new MyHttp(MainApp.getApp()));
                ServiceManager.addService("Preference", MainApp.getApp()
                        .getSharedPreferences(MainApp.getApp().getPackageName(),
                                Context.MODE_PRIVATE));
            }

            @Override
            public void onDestroy() {
                MyHttp myHttp = ServiceManager.getService("MdmHttp");
                myHttp.cancel();
            }
        };
    }
}
