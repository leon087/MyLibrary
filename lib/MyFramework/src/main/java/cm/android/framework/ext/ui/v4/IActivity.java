package cm.android.framework.ext.ui.v4;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import cm.android.framework.ext.ui.IUiInterface;

/**
 * Activity抽象接口
 */
interface IActivity extends IUiInterface {
    /**
     * setContentView()之前执行，可用于设置全屏，去除标题栏等参数
     */
    public void onBeforeContentView(Bundle savedInstanceState);

    /**
     * setContentView()之后执行
     *
     * @param savedInstanceState
     */
    public void onCreateActivity(Bundle savedInstanceState);

    /**
     * 在onDestroy()前执行
     */
    public void onDestroyActivity();

    // public int getRootViewId();

    /**
     * 弹出对话框
     *
     * @param dialogFragment
     */
    public void showDialog(Bundle arguments, DialogFragment dialogFragment);
}
