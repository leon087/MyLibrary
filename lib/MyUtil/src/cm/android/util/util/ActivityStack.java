package cm.android.util.util;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Activity栈
 */
public class ActivityStack {
	private LinkedList<Activity> activityList = ObjectUtil.newLinkedList();

	private ActivityStack() {
	}

	/**
	 * 单例模式中获取唯一的实例
	 */
	public static ActivityStack getInstance() {
		return ActivityStackHolder.INSTANCE;
	}

	private static final class ActivityStackHolder {
		private static final ActivityStack INSTANCE = new ActivityStack();
	}

	/**
	 * 添加Activity到栈中中
	 * 
	 * @param activity
	 */
	public synchronized void addActivity(Activity activity) {
		activityList.addFirst(activity);
	}

	/**
	 * 从栈中移除Activity
	 * 
	 * @param activity
	 */
	public synchronized void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	/**
	 * 移除栈顶的Activity
	 */
	public synchronized void removeTopActivity() {
		if (activityList.size() > 0) {
			Activity currentActivity = activityList.removeFirst();
			currentActivity.finish();
		}
	}

	/**
	 * 遍历所有Activity并finish
	 */
	public synchronized void finishAll() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		activityList.clear();
	}
}
