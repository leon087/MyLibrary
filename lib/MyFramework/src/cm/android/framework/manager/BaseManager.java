package cm.android.framework.manager;

import android.content.Context;
import cm.android.util.MyLog;

/**
 * Manager基类
 */
public abstract class BaseManager implements IManager {
	private volatile boolean isStart = false;

	@Override
	public final synchronized void create(Context context) {
		MyLog.i("isStart = " + isStart);
		if (isStart) {
			return;
		}
		isStart = true;
		ServiceHolder.clear();
		onCreate(context);
	}

	@Override
	public final synchronized void destroy() {
		MyLog.i("isStart = " + isStart);
		if (!isStart) {
			return;
		}
		isStart = false;
		onDestroy();
		ServiceHolder.clear();
	}

	/**
	 * 初始化资源
	 * 
	 * @param context
	 */
	protected abstract void onCreate(Context context);

	/**
	 * 与onCreate对应，释放资源
	 */
	protected abstract void onDestroy();

	/**
	 * 初始化管理模块
	 * 
	 * @param manager
	 */
	protected final void addService(Object manager) {
		ServiceHolder.addService(manager);
	}

	// public Object getService(Class<?> clazz) {
	// return mServices.get(clazz.getSimpleName());
	// }
}
