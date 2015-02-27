package cm.android.common.am;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cm.android.applications.AppUtil;
import cm.android.sdk.content.BaseBroadcastReceiver;
import cm.java.util.ObjectUtil;
import cm.java.util.Utils;

public class AppManager {

    private static final Logger logger = LoggerFactory.getLogger("am");

    /**
     * 通知app列表有变化
     */
    public static final int APPLIST_CHANGED = 0;

    /**
     * 请求更新列表
     */
    public static final int REQUEST_UPDATE_LIST = APPLIST_CHANGED + 1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APPLIST_CHANGED:
                    // app列表有变化
                    notifyAppListener();
                    break;
                case REQUEST_UPDATE_LIST:
                    updateAppManager.request(getAppList());
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
            handler.sendEmptyMessage(APPLIST_CHANGED);
            updateAppManager.start();
        }
    };

    private final Set<IAppListener> listeners = ObjectUtil.newHashSet();

    private final Map<String, Map<String, String>> appMap = ObjectUtil
            .newHashMap();

    private ExecutorService threadPool;

    private Context context;

    private UpdateAppManager updateAppManager;

    private PackageIntentReceiver packageIntentReceiver;

    private IAppInfoProcessor appInfoProcessor;

    public AppManager() {
        packageIntentReceiver = new PackageIntentReceiver();
    }

    public void setAppInfoProcessor(IAppInfoProcessor appInfoProcessor) {
        this.appInfoProcessor = appInfoProcessor;
    }

    public void init(Context context) {
        threadPool = Executors.newCachedThreadPool();
        updateAppManager = new UpdateAppManager(handler);
        packageIntentReceiver.register(context);
        this.context = context;
    }

    public void deInit() {
        packageIntentReceiver.unregister();
        updateAppManager.stop();
        threadPool.shutdown();
        context = null;
        updateAppManager = null;
    }

    public void initInstalledList() {
        // 异步
        // 从缓存中读取，同时从系统中读取，返回成功则更新缓存，并更新UI

        // 系统中读取
        threadPool.submit(queryAppsTask);
    }

    private void addPackage(String pkgName) {
        PackageInfo packageInfo = AppUtil.getPackageInfo(
                context.getPackageManager(), pkgName);
        if (packageInfo != null) {
            Map<String, String> map = getAppInfo(packageInfo);
            put(packageInfo.packageName, map);
            handler.sendEmptyMessage(APPLIST_CHANGED);
        }
    }

    private void removePackage(String pkgName) {
        remove(pkgName);
        handler.sendEmptyMessage(APPLIST_CHANGED);
    }

    public List<Map<String, String>> getAppList() {
        synchronized (appMap) {
            return new ArrayList<Map<String, String>>(appMap.values());
        }
    }

    private void put(String packageName, Map<String, String> app) {
        // 过滤掉自身
        if (packageName.equals(context.getPackageName())) {
            return;
        }
        synchronized (appMap) {
            appMap.put(packageName, app);
            updateAppManager.onAddApp(packageName, app);
        }
    }

    private void remove(String packageName) {
        synchronized (appMap) {
            appMap.remove(packageName);
            updateAppManager.onRemoveApp(packageName);
        }
    }

    public UpdateAppManager getUpdateAppManager() {
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

    public void notifyAppListener() {
        synchronized (listeners) {
            for (IAppListener listener : listeners) {
                listener.notifyAppListener();
            }
        }
    }

    /**
     * 获取已安装应用列表
     */
    private void initInstalledApp() {
        synchronized (appMap) {
            appMap.clear();
            /* 已安装应用列表 数据 */
            List<PackageInfo> packageList = AppUtil.getInstalledPackages(
                    context.getPackageManager(), AppUtil.APP_USER);
            if (!Utils.isEmpty(packageList)) {
                for (PackageInfo packageInfo : packageList) {
                    Map<String, String> appInfo = getAppInfo(packageInfo);
                    put(packageInfo.packageName, appInfo);
                }
            }
            logger.info("appMap = " + appMap);
        }
    }

    private Map<String, String> getAppInfo(PackageInfo packageInfo) {
        if (appInfoProcessor == null) {
            appInfoProcessor = new DefaultAppInfoProcessor();
        }
        return appInfoProcessor.getAppInfo(packageInfo);
    }

    public static interface IAppInfoProcessor {

        public Map<String, String> getAppInfo(PackageInfo packageInfo);
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

    public class DefaultAppInfoProcessor implements IAppInfoProcessor {

        @Override
        public Map<String, String> getAppInfo(PackageInfo packageInfo) {
            Map<String, String> appInfo = ObjectUtil.newHashMap();
            appInfo.put(AppTag.PACKAGE_NAME, packageInfo.packageName);
            appInfo.put(AppTag.VERSION_CODE,
                    String.valueOf(packageInfo.versionCode));
            return appInfo;
        }

    }
}
