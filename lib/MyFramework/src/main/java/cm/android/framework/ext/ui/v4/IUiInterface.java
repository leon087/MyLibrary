package cm.android.framework.ext.ui.v4;

import android.os.Bundle;
import android.view.View;

interface IUiInterface {

    /**
     * 初始化View
     *
     * @param rootView 根view
     */
    public void initView(View rootView, Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    public void initData(Bundle savedInstanceState);

    /**
     * 设置根view的id
     */
    public int getRootViewId();
}
