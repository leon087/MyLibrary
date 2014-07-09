package cm.android.app.ui.fragment.dialog;

import android.os.Bundle;
import android.view.View;

public interface IBaseDialogInterface {
	/**
	 * 初始化View
	 * 
	 * @param rootView
	 *            根view
	 * @param bundle
	 */
	public void setupView(View rootView, Bundle bundle);

	/**
	 * 初始化数据
	 * 
	 * @param bundle
	 */
	public void setupData(Bundle bundle);

	/**
	 * 设置根view的id
	 * 
	 * @return
	 */
	public int getConvertViewId();

}
