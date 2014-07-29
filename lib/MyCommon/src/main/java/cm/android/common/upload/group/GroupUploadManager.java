package cm.android.common.upload.group;

import android.content.Context;
import android.os.Message;
import cm.android.common.db.MyDaoManager;
import cm.android.common.ui.callback.UICallback;
import cm.android.common.ui.callback.UIObserver;
import cm.android.common.upload.*;
import cm.android.common.upload.db.GroupUploadBean;
import cm.android.common.upload.db.GroupUploadDao;
import cm.android.thread.LooperHandler;
import cm.android.thread.ThreadPool;
import cm.android.thread.ThreadUtil;
import cm.android.util.ObjectUtil;

import java.util.Iterator;
import java.util.List;

public class GroupUploadManager implements UIObserver {
    private int activeCount;
    private ThreadPool threadPool = new ThreadPool(1, 1);
    // private Map<UploadQueue, UploadQueueTask> uploadMap = Collections
    // .synchronizedMap(new LinkedHashMap<UploadQueue, UploadQueueTask>());

    private List<GroupUploadItem> uploadList = ObjectUtil.newArrayList();
    private GroupUploadDao groupUploadDao = null;
    private MyDaoManager daoManager = null;
    private List<UICallback> mUICallbacks = ObjectUtil.newArrayList();
    private IUpload upload;

    /**
     * 初始化
     *
     * @param activeCount 允许同时进行任务数
     * @param context
     */
    public GroupUploadManager(int activeCount, Context context, IUpload upload) {
        daoManager = new MyDaoManager().init(context);
        this.groupUploadDao = daoManager.getDao(GroupUploadDao.class);
        this.activeCount = activeCount;
        this.upload = upload;
        init();
    }

    public IUpload getUpload() {
        return upload;
    }

    public synchronized void release() {
        looperHandler.release();
        uploadList.clear();
        mUICallbacks.clear();
    }

    private void init() {
        // 从数据库中读取数据进行初始化
    }

    public long getActiveCount() {
        return threadPool.getActiveCount();
    }

    public synchronized void startTask(GroupUploadItem item) {
        // 准备下载时首先设为等待状态，等真正开始下载时才设为下载状态
        GroupUploadBean bean = item.bean;
        bean.setStatus(UploadStatus.WAITING);
        item.start();
        if (item.groupTask == null) {
            // 创建tasklist
            item.groupTask = new GroupUploadTask(item, daoManager,
                    uploadListener);
        }
        if (threadPool.getActiveCount() < activeCount) {
            threadPool.execute(item.groupTask);
        }
        // 如果速度线程未开启，则开启速度计算线程
        // if (!mSpeedClacTask.getActive()) {
        // mSpeedClacTask.setActive(true);
        // new Thread(mSpeedClacTask).start();
        // }

        if (!uploadList.contains(item)) {
            uploadList.add(item);
        }
        // 更新数据库
        groupUploadDao.saveOrUpdate(bean);
    }

    public synchronized void stopTask(GroupUploadItem item, int status) {
        GroupUploadBean bean = item.bean;
        item.stop();
        ThreadUtil.sleep(100);
        // 更新数据库
        groupUploadDao.saveOrUpdate(bean);

        processSpeedTask();
        startNextTask();
    }

    public synchronized void deleteTask(GroupUploadItem item) {
        uploadList.remove(item);
        item.delete();
        threadPool.remove(item.groupTask);
        // 删除文件和数据库记录
        deleteRecord(item);
        ThreadUtil.sleep(100);

        processSpeedTask();
        startNextTask();
    }

    private synchronized void startNextTask() {
        Iterator<GroupUploadItem> it = uploadList.iterator();
        while (it.hasNext()) {
            GroupUploadItem groupUploadItem = it.next();
            // downloadMap.get(downloadBeanTmp);
            if (groupUploadItem.isWaiting()) {
                startTask(groupUploadItem);
                return;
            }
        }
    }

    private void deleteRecord(GroupUploadItem item) {
        item.groupTask.deleteRecord();
        groupUploadDao.delete(item.bean);

    }

    private void processSpeedTask() {

    }

    private void notifyUI(int what, Object obj) {
        for (UICallback callback : mUICallbacks) {
            callback.notifyUI(what, obj);
        }
    }

    @Override
    public void register(UICallback callback) {
        mUICallbacks.add(callback);
    }

    @Override
    public void unRegister(UICallback callback) {
        mUICallbacks.remove(callback);
    }

    private IUploadListener<GroupUploadTask> uploadListener = new IUploadListener<GroupUploadTask>() {

        @Override
        public void onUploading(GroupUploadTask task, float percent) {

        }

        @Override
        public void onUploadStop(GroupUploadTask task) {
            startNextTask();
        }

        @Override
        public void onUploadStart(GroupUploadTask task) {

        }

        @Override
        public void onUploadFailed(GroupUploadTask task, UploadException e) {
            startNextTask();

            notifyUI(UploadStatus.FAILED, task);
        }

        @Override
        public void onUploadCompleted(GroupUploadTask task) {
            startNextTask();
            // 发送上传成功的请求
            UploadItem item = task.getItem().getItems().get(0);
            looperHandler.sendMessage(UPLOAD_COMPLETE, item);

            notifyUI(UploadStatus.COMPLETED, task);
        }
    };

    private static final int UPLOAD_COMPLETE = 0x01;

    private LooperHandler looperHandler = new LooperHandler(
            new android.os.Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case UPLOAD_COMPLETE:
                            UploadItem item = (UploadItem) msg.obj;
                            item.upload.onUploadComplete(item.uploadBean);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });

}
