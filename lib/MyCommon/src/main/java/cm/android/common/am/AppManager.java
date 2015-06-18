package cm.android.common.am;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cm.android.applications.AppUtil;
import cm.android.sdk.MyHandler;
import cm.android.sdk.content.BaseBroadcastReceiver;
import cm.java.util.MapUtil;
import cm.java.util.ObjectUtil;
import cm.java.util.Utils;

public class AppManager {

    private static final Logger logger = LoggerFactory.getLogger("am");

    /**
     * 通知app列表有变化
     */
    public static final int APPLIST_CHANGED = 0;

    private MyHandler handler = new MyHandler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APPLIST_CHANGED:
                    // app列表有变化
                    notifyAppListener();
                    break;
                default:
                    break;
            }
        }
    };

    // 查询已安装的apps
    private Runnable queryAppsTask = new Runnable() {
        @Override
        public void run() {
            initInstalledApp();
            updateAppManager.initSucceed();
            handler.sendEmptyMessage(APPLIST_CHANGED);
        }
    };

    private final Set<IAppListener> listeners = ObjectUtil.newHashSet();

    private final Map<String, Map<String, String>> appMap = ObjectUtil
            .newHashMap();

    private ExecutorService threadPool;

    private Context context;

    private final UpdateAppManager updateAppManager = new UpdateAppManager();

    private final PackageIntentReceiver packageIntentReceiver = new PackageIntentReceiver();

    private IAppProcessor appProcessor;

    public AppManager() {
    }

    private void configAppProcessor(IAppProcessor appProcessor) {
        if (appProcessor == null) {
            this.appProcessor = new DefaultAppProcessor();
        } else {
            this.appProcessor = appProcessor;
        }
    }

    void init(Context context, IAppProcessor appProcessor,
            UpdateAppManager.IAsynRequest aysnRequest) {
        this.context = context;

        threadPool = Executors.newCachedThreadPool();
        updateAppManager.init(aysnRequest);
        configAppProcessor(appProcessor);
        packageIntentReceiver.register(context);
    }

    void deInit() {
        packageIntentReceiver.unregister();
        updateAppManager.deInit();
        threadPool.shutdown();
        context = null;
        appProcessor = null;
    }

    void initInstalledList() {
        // 异步
        // 从缓存中读取，同时从系统中读取，返回成功则更新缓存，并更新UI

        // 系统中读取
        threadPool.submit(queryAppsTask);
    }

    private void addPackage(String pkgName) {
        PackageInfo packageInfo = AppUtil.getPackageInfo(
                context.getPackageManager(), pkgName);
        if (packageInfo != null) {
            Map<String, String> map = appProcessor.getAppInfo(packageInfo);
            boolean result = put(packageInfo.packageName, map);
            if (result) {
                handler.sendEmptyMessage(APPLIST_CHANGED);
            }
        }
    }

    private void removePackage(String pkgName) {
        boolean result = remove(pkgName);
        if (result) {
            handler.sendEmptyMessage(APPLIST_CHANGED);
        }
    }

    public List<Map<String, String>> getAppList() {
        synchronized (appMap) {
            return new ArrayList<Map<String, String>>(appMap.values());
        }
    }

    private boolean put(String packageName, Map<String, String> app) {
        if (Utils.isEmpty(app)) {
            return false;
        }

        // 过滤掉自身
        if (packageName.equals(context.getPackageName())) {
            return false;
        }
        //过滤掉不在白名单中的应用

        int versionCode = MapUtil.getInt(app, AppTag.VERSION_CODE);
        updateAppManager.onAddApp(packageName, versionCode);
        synchronized (appMap) {
            appMap.put(packageName, app);

            return true;
        }
    }

    private boolean remove(String packageName) {
        updateAppManager.onRemoveApp(packageName);
        synchronized (appMap) {
            Map<String, String> tmp = appMap.remove(packageName);
            if (Utils.isEmpty(tmp)) {
                return false;
            } else {
                return true;
            }
        }
    }

    UpdateAppManager getUpdateAppManager() {
        return updateAppManager;
    }

    public void registerAppListener(IAppListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * 去注册IAppManager监听接口
     */
    public void unregisterAppListener(IAppListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void notifyAppListener() {
        synchronized (listeners) {
            for (IAppListener listener : listeners) {
                listener.notifyAppListener();
            }
        }
    }

    /**
     * 获取已安装应用列表
     *
     * @return
     */

//    private void initInstalledApp() {
//        synchronized (appMap) {
//            appMap.clear();
//            /* 已安装应用列表 数据 */
//            List<PackageInfo> packageList = AppUtil.getInstalledPackages(
//                    context.getPackageManager(), AppUtil.APP_USER);
//            if (!Utils.isEmpty(packageList)) {
//                for (PackageInfo packageInfo : packageList) {
//                    Map<String, String> appInfo = getAppInfo(packageInfo);
//                    put(packageInfo.packageName, appInfo);
//                }
//            }
//            logger.info("appMap = " + appMap);
//        }
//    }


    /**
     * 获取本地数据库中的应用列表
     */
    private void initInstalledApp() {
        synchronized (appMap) {
            appMap.clear();
            List<PackageInfo> packageList = appProcessor.queryApps(context);
            logger.error("ggggg packageList = " + packageList);
            if (!Utils.isEmpty(packageList)) {
                for (PackageInfo packageInfo : packageList) {
                    Map<String, String> appInfo = appProcessor.getAppInfo(packageInfo);
//                    put(packageInfo.packageName, appInfo);
                    if (packageInfo.packageName.equals(context.getPackageName())) {
                        continue;
                    }
                    appMap.put(packageInfo.packageName, appInfo);
                    int versionCode = MapUtil.getInt(appInfo, AppTag.VERSION_CODE);
                    updateAppManager.addApp(packageInfo.packageName, versionCode);
                }
            }
        }
    }

    public static interface IAppProcessor {

        public Map<String, String> getAppInfo(PackageInfo packageInfo);

        public List<PackageInfo> queryApps(Context context);
    }

    /**
     * Receives notifications when applications are added/removed.
     */
    private class PackageIntentReceiver extends BaseBroadcastReceiver {

        public PackageIntentReceiver() {

        }

        @Override
        public IntentFilter createIntentFilter() {
            IntentFilter filter = super.createIntentFilter();
            filter.addAction(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            // filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            filter.addDataScheme("package");
            // Register for events related to sdcard installation.
            // filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            // filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            return filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionStr = intent.getAction();
            String pkgName = "";
            if (Intent.ACTION_PACKAGE_ADDED.equals(actionStr)) {
                Uri data = intent.getData();
                pkgName = data.getEncodedSchemeSpecificPart();
                addPackage(pkgName);
            } else if (Intent.ACTION_PACKAGE_REMOVED.equals(actionStr)) {
                Uri data = intent.getData();
                pkgName = data.getEncodedSchemeSpecificPart();
                removePackage(pkgName);
            }
            logger.info("pkgName = " + pkgName + ",intent = " + intent);
        }
    }

    public static class DefaultAppProcessor implements IAppProcessor {

        @Override
        public Map<String, String> getAppInfo(PackageInfo packageInfo) {
            Map<String, String> appInfo = ObjectUtil.newHashMap();
            appInfo.put(AppTag.PACKAGE_NAME, packageInfo.packageName);
            appInfo.put(AppTag.VERSION_CODE,
                    String.valueOf(packageInfo.versionCode));
            return appInfo;
        }

        @Override
        public List<PackageInfo> queryApps(Context context) {
            return AppUtil.getInstalledPackages(context.getPackageManager());
        }

    }
}
