package wd.android.common.upload;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wd.android.common.db.MyDaoManager;
import wd.android.common.ui.callback.UICallback;
import wd.android.common.upload.db.UploadBean;
import wd.android.common.upload.db.UploadDao;
import wd.android.util.thread.ThreadPool;
import wd.android.util.thread.ThreadUtil;
import wd.android.util.util.ObjectUtil;
import android.content.Context;

public class UploadManager {
	private static final int MAX_ACTIVECOUNT = 5;
	private int activeCount = MAX_ACTIVECOUNT;
	private ThreadPool threadPool = null;
	// private Map<UploadQueue, UploadQueueTask> uploadMap = Collections
	// .synchronizedMap(new LinkedHashMap<UploadQueue, UploadQueueTask>());

	private List<UploadItem> uploadList = ObjectUtil.newArrayList();
	private UploadDao uploadDao = null;
	private MyDaoManager daoManager = null;
	private List<UICallback> mUICallbacks = new ArrayList<UICallback>();
	private IUpload upload;

	/**
	 * 初始化
	 * 
	 * @param activeCount
	 *            允许同时进行任务数
	 * @param context
	 */
	public UploadManager(int activeCount, Context context, IUpload upload) {
		daoManager = new MyDaoManager().init(context);
		this.uploadDao = daoManager.getDao(UploadDao.class);
		this.activeCount = activeCount;
		threadPool = new ThreadPool(activeCount, activeCount);
		this.upload = upload;
		init();
	}

	private void init() {
		// 从数据库中读取数据进行初始化
	}

	public void release() {
		synchronized (uploadList) {
			uploadList.clear();
		}
		synchronized (mUICallbacks) {
			mUICallbacks.clear();
		}
		threadPool.release();
		daoManager.deInit();
	}

	public long getActiveCount() {
		return threadPool.getActiveCount();
	}

	public synchronized void startTask(UploadItem item) {
		// 准备下载时首先设为等待状态，等真正开始下载时才设为下载状态
		UploadBean bean = item.uploadBean;
		bean.setStatus(UploadStatus.WAITING);
		item.start();
		if (item.uploadTask == null) {
			// 创建tasklist
			item.uploadTask = new UploadTask(item, uploadListener, uploadDao,
					null);
		}
		if (threadPool.getActiveCount() < activeCount) {
			threadPool.execute(item.uploadTask);
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
		uploadDao.saveOrUpdate(bean);
	}

	public synchronized void stopTask(UploadItem item, int status) {
		item.stop();
		ThreadUtil.sleep(100);
		// 更新数据库
		uploadDao.saveOrUpdate(item.uploadBean);

		processSpeedTask();
		startNextTask();
	}

	public synchronized void deleteTask(UploadItem item) {
		uploadList.remove(item);
		item.delete();
		threadPool.remove(item.uploadTask);
		// 删除文件和数据库记录
		deleteRecord(item);
		ThreadUtil.sleep(100);

		processSpeedTask();
		startNextTask();
	}

	private synchronized void startNextTask() {
		Iterator<UploadItem> it = uploadList.iterator();
		while (it.hasNext()) {
			UploadItem uploadItem = it.next();
			// downloadMap.get(downloadBeanTmp);
			if (uploadItem.isWaiting()) {
				startTask(uploadItem);
				return;
			}
		}
	}

	private void deleteRecord(UploadItem item) {
		uploadDao.delete(item.uploadBean);

	}

	private void processSpeedTask() {

	}

	private IUploadListener<UploadTask> uploadListener = new IUploadListener<UploadTask>() {

		@Override
		public void onUploading(UploadTask task, float percent) {

		}

		@Override
		public void onUploadStop(UploadTask task) {
			startNextTask();
		}

		@Override
		public void onUploadStart(UploadTask task) {

		}

		@Override
		public void onUploadFailed(UploadTask task, UploadException e) {
			startNextTask();
		}

		@Override
		public void onUploadCompleted(UploadTask task) {
			startNextTask();
		}
	};

}
