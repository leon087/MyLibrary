package cm.android.util;

import android.app.Activity;

import java.util.LinkedList;

import cm.java.util.ObjectUtil;

/**
 * Activity栈
 */
@Deprecated
public final class ActivityStack {

    private static final LinkedList<Activity> ACTIVITY_LIST = ObjectUtil.newLinkedList();

    private ActivityStack() {
    }

    /**
     * 单例模式中获取唯一的实例
     */
    public static ActivityStack getInstance() {
        return ActivityStackHolder.INSTANCE;
    }

    /**
     * 添加Activity到栈中中
     */
    public synchronized void addActivity(Activity activity) {
        ACTIVITY_LIST.addFirst(activity);
    }

    /**
     * 从栈中移除Activity
     */
    public synchronized void removeActivity(Activity activity) {
        ACTIVITY_LIST.remove(activity);
    }

    /**
     * 移除栈顶的Activity
     */
    public synchronized void removeTopActivity() {
        if (ACTIVITY_LIST.size() > 0) {
            Activity currentActivity = ACTIVITY_LIST.removeFirst();
            currentActivity.finish();
        }
    }

    /**
     * 遍历所有Activity并finish
     */
    public synchronized void finishAll() {
        for (Activity activity : ACTIVITY_LIST) {
            activity.finish();
        }
        ACTIVITY_LIST.clear();
    }

    private static final class ActivityStackHolder {

        private static final ActivityStack INSTANCE = new ActivityStack();
    }
}
