package wd.android.common.download;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import wd.android.common.db.MyDaoManager;
import wd.android.common.download.db.DownloadBean;
import wd.android.common.download.db.DownloadDao;
import wd.android.common.ui.callback.UICallback;
import wd.android.util.thread.ThreadPool;
import wd.android.util.thread.ThreadUtil;
import wd.android.util.util.IoUtil;
import wd.android.util.util.MyLog;
import wd.android.util.util.ObjectUtil;
import wd.android.util.util.Utils;
import android.content.Context;

/**
 * 下载管理对象
 */
public class DownloadManager {
	private static final int MAX_ACTIVECOUNT = 5;
	private int activeCount = MAX_ACTIVECOUNT;
	private ThreadPool threadPool = null;

	private List<DownloadItem> downloadList = ObjectUtil.newArrayList();
	private SpeedClacTask mSpeedClacTask = new SpeedClacTask();
	private DownloadDao downloadDao = null;
	private MyDaoManager daoManager = null;
	private List<UICallback> mUICallbacks = new ArrayList<UICallback>();
	private IOnCompleted onCompleted;

	/**
	 * 初始化
	 * 
	 * @param activeCount
	 *            允许同时进行下载的任务数
	 * @param context
	 */
	public DownloadManager(int activeCount, Context context) {
		daoManager = new MyDaoManager().init(context);
		this.downloadDao = daoManager.getDao(DownloadDao.class);
		this.activeCount = activeCount;
		threadPool = new ThreadPool(activeCount, activeCount);
		init();
	}

	public void setOnCompleted(IOnCompleted onCompleted) {
		this.onCompleted = onCompleted;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		mSpeedClacTask.setActive(false);
		synchronized (downloadList) {
			downloadList.clear();
		}
		synchronized (mUICallbacks) {
			mUICallbacks.clear();
		}
		threadPool.release();
		daoManager.deInit();
	}

	/**
	 * 注册UI监听
	 */
	public void registerListener(UICallback uiCallback) {
		synchronized (mUICallbacks) {
			mUICallbacks.add(uiCallback);
		}
	}

	/**
	 * 去注册UI监听
	 * 
	 * @param uiCallback
	 */
	public void unRegisterListener(UICallback uiCallback) {
		synchronized (mUICallbacks) {
			mUICallbacks.remove(uiCallback);
		}
	}

	private void notifyUICallback() {
		synchronized (mUICallbacks) {
			for (UICallback uiCallback : mUICallbacks) {
				// notifyUI中刷新UI时可以判断handler中msg来减少冗余处理
				uiCallback.notifyUI(0, null);
			}
		}
	}

	private void init() {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				// 从数据库中读取数据进行初始化
				synchronized (DownloadManager.this) {
					List<DownloadBean> list = downloadDao.queryAll();
					if (!Utils.isEmpty(list)) {
						// downloadList.add
						for (DownloadBean downloadBean : list) {
							DownloadItem item = new DownloadItem();
							item.downloadBean = downloadBean;
							downloadList.add(item);
						}
					}
				}
			}
		});
	}

	/**
	 * 计算下载速度
	 */
	private class SpeedClacTask implements Runnable {
		// 正在下载镜像列表
		private LinkedHashMap<DownloadItem, Long> mirrorDownloadMap = new LinkedHashMap<DownloadItem, Long>();

		private boolean active = false;

		private static final int SECOND = 1000;
		/** 2s */
		private static final int PERIOD = 2;

		public void setActive(boolean active) {
			this.active = active;
		}

		public boolean getActive() {
			return active;
		}

		@Override
		public void run() {
			while (active) {
				synchronized (DownloadManager.this) {
					// 遍历镜像列表和正在下载task列表，镜像列表和正在下载列表有相同对象则计算下载速度，没有则速度为0
					Iterator<DownloadItem> it = downloadList.iterator();
					while (it.hasNext()) {
						DownloadItem downloadItem = (DownloadItem) it.next();
						if (mirrorDownloadMap.containsKey(downloadItem)) {
							// 计算速度
							downloadItem.speed = calcSpeed(PERIOD * SECOND,
									downloadItem.downloadBean.getDownLength(),
									mirrorDownloadMap.get(downloadItem));
						} else {
							// 速度为0
							downloadItem.speed = 0;
						}
						// 保存至镜像
						mirrorDownloadMap.remove(downloadItem);
						mirrorDownloadMap.put(downloadItem,
								downloadItem.downloadBean.getDownLength());
					}
					// 放在while循环外可以减少刷新帧数
					notifyUICallback();
				}
				ThreadUtil.sleep(PERIOD * SECOND);
			}
			mirrorDownloadMap.clear();
		}

		// 线程队列中DownloadTask有正在运行时，则开启每秒循环线程，计算正在下载的task中bean的速度
		// 开启线程每秒统计进度，算出下载速度
		private long calcSpeed(int second, Long newSize, Long oldSize) {
			/* 计算下载速度(byte/s) */
			long speed = (newSize - oldSize) / second;
			if (speed < 0) {
				MyLog.e("newSize = " + newSize + ",oldSize = " + oldSize
						+ ",second = " + second);
				speed = 0;
			}
			return speed;
		}
	}

	/**
	 * 获取下载队列中的DownloadItem列表
	 * 
	 * @return
	 */
	public synchronized List<DownloadItem> getDownloadList() {
		List<DownloadItem> list = new ArrayList<DownloadItem>(downloadList);
		return list;
	}

	/**
	 * 开始下载原子方法
	 * 
	 * @param downloadBean
	 */
	public synchronized void startTask(DownloadItem item) {
		// 准备下载时首先设为等待状态，等真正开始下载时才设为下载状态
		DownloadBean bean = item.downloadBean;
		bean.setStatus(DownloadStatus.WAITING);
		item.start();
		if (item.task == null) {
			// 创建tasklist
			item.task = new DownloadTask(downloadListener, item, downloadDao,
					null);
		}
		if (threadPool.getActiveCount() < activeCount) {
			threadPool.execute(item.task);
		}

		// 如果速度线程未开启，则开启速度计算线程
		if (!mSpeedClacTask.getActive()) {
			mSpeedClacTask.setActive(true);
			new Thread(mSpeedClacTask).start();
		}

		if (!downloadList.contains(item)) {
			downloadList.add(item);
		}
		// 更新数据库
		downloadDao.saveOrUpdate(bean);
	}

	/**
	 * 暂停下载原子方法
	 */
	public synchronized void stopTask(DownloadItem item, int status) {
		item.stop();
		ThreadUtil.sleep(100);
		// 更新数据库
		downloadDao.saveOrUpdate(item.downloadBean);

		processSpeedTask();
		startNextTask();
	}

	/**
	 * 删除下载对象
	 */
	public synchronized void deleteTask(DownloadItem item) {
		downloadList.remove(item);
		item.delete();
		threadPool.remove(item.task);
		// 删除文件和数据库记录
		deleteRecord(item);
		ThreadUtil.sleep(100);

		processSpeedTask();
		startNextTask();
	}

	// 下载下一个等待列表中的对象
	private synchronized void startNextTask() {
		Iterator<DownloadItem> it = downloadList.iterator();
		int count = 0;
		while (it.hasNext()) {
			DownloadItem item = it.next();
			// downloadMap.get(downloadBeanTmp);
			if (item.isWaiting() && isDownloadStateActive(item.downloadBean)) {
				startTask(item);
				count++;
				return;
			}
		}
		if (count == 0) {
			mSpeedClacTask.setActive(false);
		}
	}

	/**
	 * 删除文件和数据库记录
	 * 
	 * @param downloadBean
	 */
	private void deleteRecord(DownloadItem downloadItem) {
		downloadDao.delete(downloadItem.downloadBean);
		IoUtil.deleteFile(downloadItem.downloadBean.getFilePath());
	}

	/**
	 * 处理下载速度计算线程
	 */
	private void processSpeedTask() {
		synchronized (DownloadManager.this) {
			// 如果下载队列中没有激活态任务（正在下载|等待下载），则退出速度线程
			Iterator<DownloadItem> it = downloadList.iterator();
			int count = 0;
			while (it.hasNext()) {
				DownloadBean downloadBeanTmp = it.next().downloadBean;
				// downloadMap.get(downloadBeanTmp);
				if (isDownloadStateActive(downloadBeanTmp)) {
					count++;
					break;
				}
			}
			if (count == 0) {
				mSpeedClacTask.setActive(false);
			}
		}
	}

	/**
	 * 判断是否处于激活状态（正在下载|等待下载）
	 * 
	 * @param downloadBean
	 * @return
	 */
	private boolean isDownloadStateActive(DownloadBean downloadBean) {
		if (downloadBean.getStatus() == DownloadStatus.WAITING
				|| downloadBean.getStatus() == DownloadStatus.RUNNING) {
			return true;
		}
		return false;
	}

	//
	private IDownloadListener<DownloadTask> downloadListener = new IDownloadListener<DownloadTask>() {
		@Override
		public void onDownloading(DownloadTask task, float percent) {
			notifyUICallback();
		}

		@Override
		public void onDownloadStop(DownloadTask task) {
			startNextTask();
			notifyUICallback();
		}

		@Override
		public void onDownloadStart(DownloadTask task) {
			// 下载前初始化，获取下载地址

			notifyUICallback();
		}

		@Override
		public void onDownloadFailed(DownloadTask task, DownloadException e) {
			startNextTask();
			notifyUICallback();
			// 下载失败，可根据DownloadException处理
		}

		@Override
		public void onDownloadCompleted(DownloadTask task) {
			startNextTask();
			notifyUICallback();
			// 重命名?
			// 安装
			if (onCompleted != null) {
				onCompleted.onDownloadCompleted(task.getDownloadItem());
			}
		}
	};

	public static interface IOnCompleted {
		void onDownloadCompleted(DownloadItem item);
	}
}
