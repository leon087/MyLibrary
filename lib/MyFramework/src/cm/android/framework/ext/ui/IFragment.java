package cm.android.framework.ext.ui;

import android.content.Intent;

interface IFragment extends IUiInterface {
    /**
     * 在Activity的OnNewIntent中调用
     *
     * @param intent
     */
    public void onNewIntent(Intent intent);

    /**
     * 设置theme
     */
    public int initThemeStyle();
}
