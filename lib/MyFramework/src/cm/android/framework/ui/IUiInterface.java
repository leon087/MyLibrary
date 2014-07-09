package cm.android.framework.ui;

import android.os.Bundle;
import android.view.View;

interface IUiInterface {
	/**
	 * 初始化View
	 * 
	 * @param rootView
	 *            根view
	 * @param savedInstanceState
	 */
	public void initView(View rootView, Bundle savedInstanceState);

	/**
	 * 初始化数据
	 * 
	 * @param savedInstanceState
	 */
	public void initData(Bundle savedInstanceState);

	/**
	 * 设置根view的id
	 * 
	 * @return
	 */
	public int getRootViewId();
}
