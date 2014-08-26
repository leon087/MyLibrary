package cm.android.framework.ext.ui.v4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import cm.android.framework.ext.ui.v4.BaseActivity.KeyEventListener;
import cm.android.framework.ext.ui.IFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * android.support.v4.app.Fragment的包装类
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment
        implements IFragment, KeyEventListener {

    protected BaseActivity mActivity;
    private static final Logger logger = LoggerFactory.getLogger(BaseActivity.class);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        int layoutResID = getRootViewId();
        View fragmentView = null;
        if (layoutResID > 0) {
            int themeStyle = initThemeStyle();
            LayoutInflater themeLayoutInflater = getThemeLayoutInflater(
                    inflater, themeStyle);
            fragmentView = themeLayoutInflater.inflate(layoutResID, container,
                    false);
            initView(fragmentView, bundle);
        } else {
            fragmentView = new TextView(getActivity());
        }
        return fragmentView;
    }

    /**
     * Called when the fragment's activity has been created and this fragment's
     * view hierarchy instantiated. It can be used to do final initialization
     * once these pieces are in place, such as retrieving views or restoring
     * state. It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance, as this
     * callback tells the fragment when it is fully associated with the new
     * activity instance. This is called after {@link #onCreateView} and before
     * {@link #onViewStateRestored(Bundle)}.
     *
     * @param bundle If the fragment is being re-created from a previous saved
     *               state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initData(bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public int initThemeStyle() {
        return -1;
    }

    /**
     * Set a hint to the system about whether this fragment's UI is currently
     * visible to the user. This hint defaults to true and is persistent across
     * fragment instance state save and restore.
     * <p/>
     * <p>
     * An app may set this to false to indicate that the fragment's UI is
     * scrolled out of visibility or is otherwise not directly visible to the
     * user. This may be used by the system to prioritize operations such as
     * fragment lifecycle updates or loader ordering behavior.
     * </p>
     *
     * @param isVisibleToUser true if this fragment's UI is currently visible to the user
     *                        (default), false if it is not.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // 该方法先于onStart执行，在tab切换时会触发
        super.setUserVisibleHint(isVisibleToUser);
        if (logger.isDebugEnabled()) {
            logger.debug("isVisibleToUser = " + isVisibleToUser + ",isVisible() = "
                    + isVisible());
        }
        // onStart之后isVisible()会返回true
        if (isVisible()) {
            if (isVisibleToUser) {
                onShowToUser();
            } else {
                onUnShowToUser();
            }
        }
    }

    @Override
    public void onStop() {
        mActivity.unRegisterKeyEventListener(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.registerKeyEventListener(this);
        if (getUserVisibleHint()) {
            onShowToUser();
        }
    }

    /**
     * tab中对用户可见时调用
     */
    protected void onShowToUser() {

    }

    /**
     * tab中对用户不可见时调用
     */
    protected void onUnShowToUser() {

    }

    /**
     * 设置Fragment主题
     */
    private LayoutInflater getThemeLayoutInflater(LayoutInflater inflater,
                                                  int themeStyle) {
        if (themeStyle <= 0) {
            return inflater;
        }

        Context ctxWithTheme = new ContextThemeWrapper(getActivity(),
                themeStyle);
        // 通过生成的Context创建一个LayoutInflater
        LayoutInflater localLayoutInflater = inflater
                .cloneInContext(ctxWithTheme);
        return localLayoutInflater;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
