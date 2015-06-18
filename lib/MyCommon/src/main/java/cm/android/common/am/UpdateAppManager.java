package cm.android.common.am;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.sdk.MyHandler;
import cm.java.util.MapUtil;
import cm.java.util.ObjectUtil;
import cm.java.util.Utils;

public class UpdateAppManager {

    private static final Logger logger = LoggerFactory.getLogger("update");

    public static final String ACTION_REQUEST_UPDATE = "cm.android.intent.action.REQUEST_UPDATE";

    private static final long PERIOD = 12 * 60 * 60 * 1000;

    private final Set<IUpdateAppListener> listeners = ObjectUtil.newHashSet();

    private final MyHandler taskHandler = new MyHandler() {
        @Override
        public void handleMessage(Message message) {
            if (!initFlag.get()) {
                return;
            }

            sendMsg();
            request();
        }
    };

    private IAsynRequest asynRequest = defaultAsynRequest;

    private ExecutorService threadPool;

//    private List<Map<String, Object>> updateList = ObjectUtil.newArrayList();

    private Map<String, Map<String, Object>> updateList = ObjectUtil.newHashMap();

    private final List<UpdateAppModel> requestList = ObjectUtil.newArrayList();

    private AtomicBoolean initFlag = new AtomicBoolean(false);

    UpdateAppManager() {
        threadPool = Executors.newCachedThreadPool();
    }

    private void config(IAsynRequest aysnRequest) {
        if (aysnRequest != null) {
            this.asynRequest = aysnRequest;
        }
    }

    public List<Map<String, Object>> getUpdateList() {
        synchronized (updateList) {
            return new ArrayList<Map<String, Object>>(updateList.values());
        }
    }

    public List<String> getPackageNames() {
        synchronized (updateList) {
            return new ArrayList<String>(updateList.keySet());
        }
    }

//    void start() {
//        taskHandler.postDelayed(task, INIT_DELAY_TIME);
//    }
//
//    void stop() {
//        taskHandler.removeCallbacks(task);
//    }

    void init(IAsynRequest aysnRequest) {
        config(aysnRequest);
        listeners.clear();
        requestList.clear();
        updateList.clear();
        initFlag.set(true);
    }

    void deInit() {
        initFlag.set(false);
        threadPool.shutdown();
        taskHandler.getHandler().removeMessages(0);
        listeners.clear();
        requestList.clear();
        updateList.clear();
        this.asynRequest = defaultAsynRequest;
    }

    private void sendMsg() {
        if (!taskHandler.getHandler().hasMessages(0)) {
            taskHandler.getHandler().sendEmptyMessageDelayed(0, PERIOD);
        }
    }

    void initSucceed() {
        if (initFlag.get()) {
            sendMsg();
        }
    }

    // 第一次请求后，每两小时查询一次
    void request() {
        logger.info("request:requestList = {}", requestList);
        if (Utils.isEmpty(requestList)) {
            return;
        }

        List<UpdateAppModel> list = new ArrayList(requestList);
        asynRequest.request(list, new IResult() {
            @Override
            public void onSuccess(List<Map<String, Object>> updateAppList) {
                synchronized (updateList) {
                    updateList.clear();
                    if (!Utils.isEmpty(updateAppList)) {
                        for (Map<String, Object> map : updateAppList) {
                            String packageName = MapUtil.getString(map, AppTag.PACKAGE_NAME);
                            updateList.put(packageName, map);
                        }
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

    private void notifyUpdateAppListener() {
        synchronized (listeners) {
            for (IUpdateAppListener listener : listeners) {
                listener.notifyUpdateAppListener();
            }
        }
    }

    void addApp(String packageName, int versionCode) {
        UpdateAppModel model = new UpdateAppModel();
        model.packageName = packageName;
        model.versionCode = versionCode;
        synchronized (requestList) {
            requestList.add(model);
        }
    }

    void onAddApp(final String packageName, int installedVersion) {
        // 查询list中，如果versionCode>=list，则移除list中数据，并刷新UI
        Map<String, Object> tmp = updateList.get(packageName);
        if (tmp == null) {
            return;
        }

        int versionCode = MapUtil.getInt(tmp, AppTag.VERSION_CODE);
        if (installedVersion >= versionCode) {
            updateList.remove(packageName);
            notifyUpdateAppListener();
        }
    }

    void onRemoveApp(final String packageName) {
        // 移除list中数据，并刷新UI
        Map<String, Object> tmp = updateList.remove(packageName);
        if (tmp != null) {
            notifyUpdateAppListener();
        }
    }

    /**
     * 更新列表请求接口
     */
    public static interface IAsynRequest {

        //        public void request(List<Map<String, String>> list, IResult iResult);
        public void request(List<UpdateAppModel> list, IResult iResult);
    }

    /**
     * 更新列表返回结果接口
     */
    public static interface IResult {

        public void onSuccess(List<Map<String, Object>> updateAppList);

    }

    public static final IAsynRequest defaultAsynRequest = new IAsynRequest() {

        @Override
        public void request(List<UpdateAppModel> list, IResult iResult) {

        }
    };
}
