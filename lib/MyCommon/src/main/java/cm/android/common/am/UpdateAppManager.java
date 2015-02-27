package cm.android.common.am;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cm.java.util.MapUtil;
import cm.java.util.ObjectUtil;
import cm.java.util.Utils;

public class UpdateAppManager {

    private static final Logger logger = LoggerFactory.getLogger("update");

    private static final long INIT_DELAY_TIME = 1 * 60 * 1000 / 2;

    // private static final long PERIOD = 4 * 60 * 60 * 1000;
    private static final long PERIOD = 60 * 1000;

    private Runnable task = new Runnable() {
        public void run() {
            taskHandler.postDelayed(this, PERIOD);
            //
            externalHandler.sendEmptyMessage(AppManager.REQUEST_UPDATE_LIST);
        }
    };

    private final Set<IUpdateAppListener> listeners = ObjectUtil.newHashSet();

    private Handler taskHandler = new Handler();

    private Handler externalHandler;

    private IAsynRequest asynRequest;

    private ExecutorService threadPool;

    private List<Map<String, Object>> updateList = ObjectUtil.newArrayList();

    UpdateAppManager(Handler externalHandler) {
        this.externalHandler = externalHandler;
        threadPool = Executors.newCachedThreadPool();
    }

    public void config(IAsynRequest aysnRequest) {
        this.asynRequest = aysnRequest;
    }

    public List<Map<String, Object>> getUpdateList() {
        synchronized (updateList) {
            return new ArrayList<Map<String, Object>>(updateList);
        }
    }

    void start() {
        taskHandler.postDelayed(task, INIT_DELAY_TIME);
    }

    void stop() {
        taskHandler.removeCallbacks(task);
    }

    // 第一次请求后，每两小时查询一次
    void request(List<Map<String, String>> list) {
        if (asynRequest == null) {
            // MyLog.e("aysnRequest = null");
            asynRequest = new DefaultAsynRequest();
        }

        logger.info("request list = " + list);
        asynRequest.request(list, new IResult() {
            @Override
            public void onSuccess(List<Map<String, Object>> updateAppList) {
                synchronized (updateList) {
                    updateList.clear();
                    if (!Utils.isEmpty(updateAppList)) {
                        updateList.addAll(updateAppList);
                    }
                }

                notifyUpdateAppListener();
            }
        });
    }

    public void registerUpdateAppListener(IUpdateAppListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * 去注册IAppManager监听接口
     */
    public void unregisterUpdateAppListener(IUpdateAppListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public void notifyUpdateAppListener() {
        synchronized (listeners) {
            for (IUpdateAppListener listener : listeners) {
                listener.notifyUpdateAppListener();
            }
        }
    }

    public void onAddApp(final String packageName, final Map<String, String> app) {
        // 查询list中，如果versionCode>=list，则移除list中数据，并刷新UI
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (updateList) {
                    Map<String, Object> mapTmp = null;
                    for (Map<String, Object> map : updateList) {
                        String pkgName = MapUtil.getString(map,
                                AppTag.PACKAGE_NAME);
                        if (pkgName.equals(packageName)) {
                            int versionCode = MapUtil.getInt(map,
                                    AppTag.VERSION_CODE);
                            int installedVersion = MapUtil.getInt(app,
                                    AppTag.VERSION_CODE);
                            if (installedVersion >= versionCode) {
                                mapTmp = map;
                            }
                            break;
                        }
                    }
                    if (mapTmp != null) {
                        updateList.remove(mapTmp);
                        notifyUpdateAppListener();
                    }
                }
            }
        });
    }

    public void onRemoveApp(final String packageName) {
        // 移除list中数据，并刷新UI
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (updateList) {
                    Map<String, Object> mapTmp = null;
                    for (Map<String, Object> map : updateList) {
                        String pkgName = MapUtil.getString(map,
                                AppTag.PACKAGE_NAME);
                        if (pkgName.equals(packageName)) {
                            mapTmp = map;
                            break;
                        }
                    }
                    if (mapTmp != null) {
                        updateList.remove(mapTmp);
                        notifyUpdateAppListener();
                    }
                }
            }
        });
    }

    public static interface IAsynRequest {

        public void request(List<Map<String, String>> list, IResult iResult);
    }

    public static interface IResult {

        public void onSuccess(List<Map<String, Object>> updateAppList);
    }

    public static class DefaultAsynRequest implements IAsynRequest {

        @Override
        public void request(List<Map<String, String>> list, final IResult result) {
            // String url = MyManager.getUrl(UrlData.APP_UPDATE_URL);
            // Map<String, String> paramMap = ObjectUtil.newHashMap();
            // String json = JSON.toJSONString(list);
            // paramMap.put(Tag.REQUEST_LIST, json);
            // HttpUtil.exec(url, paramMap, new BaseHttpListener() {
            // @Override
            // protected void onSuccess(Map<String, String> headers,
            // Map<String, Object> responseMap) {
            // super.onSuccess(headers, responseMap);
            // // externalHandler
            // List<Map<String, Object>> updateAppList = MapUtil.getList(
            // responseMap, Tag.UPDATE_APP_LIST);
            // result.onSuccess(updateAppList);
            // }
            // });
        }
    }
}
