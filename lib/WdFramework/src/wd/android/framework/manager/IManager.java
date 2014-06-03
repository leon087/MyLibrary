package wd.android.framework.manager;

import android.content.Context;

public interface IManager {
	/**
	 * 创建服务
	 * 
	 * @param context
	 */
	public void create(Context context);

	/**
	 * 销毁服务
	 */
	public void destroy();
}
