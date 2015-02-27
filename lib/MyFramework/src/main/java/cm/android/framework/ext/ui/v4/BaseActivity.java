package cm.android.framework.ext.ui.v4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import java.util.Set;

import cm.android.framework.ext.util.MyIntent;
import cm.android.util.ActivityStack;
import cm.java.util.ObjectUtil;
import cm.android.util.UIUtils;

/**
 * android.support.v4.app.FragmentActivity的包装类
 */
public abstract class BaseActivity extends FragmentActivity implements
        IActivity {

    protected final Bundle bundleBak = new Bundle();

    protected FragmentHelper mFragmentHelper;

    private Set<KeyEventListener> mKeyEventListener = ObjectUtil.newHashSet();

    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class);

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyIntent.restore(savedInstanceState, bundleBak);
        ActivityStack.getInstance().addActivity(this);
        mFragmentHelper = new FragmentHelper(this.getSupportFragmentManager());
        mKeyEventListener.clear();
        onBeforeContentView(savedInstanceState);
        int layoutResID = getRootViewId();
        if (layoutResID > 0) {
            setContentView(layoutResID);
        }
        onCreateActivity(savedInstanceState);
    }

    @Override
    public void onDestroyActivity() {
    }

    @Override
    protected void onDestroy() {
        mKeyEventListener.clear();
        ActivityStack.getInstance().removeActivity(this);
        onDestroyActivity();
        mFragmentHelper = null;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logger.info("newConfig = " + newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MyIntent.restore(savedInstanceState, bundleBak);
        logger.info("bundleBak = " + bundleBak);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        MyIntent.backup(outState, bundleBak);
        super.onSaveInstanceState(outState);
        logger.info("bundleBak = " + bundleBak);
    }

    @Override
    public void onBeforeContentView(Bundle savedInstanceState) {
        // 使全屏
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setSoftInputMode(
        // WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onCreateActivity(Bundle savedInstanceState) {
        View rootView = UIUtils.getContentView(this);
        initView(rootView, savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        dispatchKeyEvent(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 注册按键监听事件
     */
    public void registerKeyEventListener(KeyEventListener listener) {
        synchronized (mKeyEventListener) {
            mKeyEventListener.add(listener);
        }
    }

    /**
     * 去除按键监听事件
     */
    public void unRegisterKeyEventListener(KeyEventListener listener) {
        synchronized (mKeyEventListener) {
            mKeyEventListener.remove(listener);
        }
    }

    private void dispatchKeyEvent(int keyCode, KeyEvent event) {
        synchronized (mKeyEventListener) {
            for (KeyEventListener listener : mKeyEventListener) {
                listener.onKeyDown(keyCode, event);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void showDialog(Bundle arguments, DialogFragment dialogFragment) {
        mFragmentHelper.showDialog(arguments, dialogFragment);
    }

    /**
     * 按键监听器
     */
    public static interface KeyEventListener {

        /**
         * key被按下时调用
         */
        boolean onKeyDown(int keyCode, KeyEvent event);
    }

}
