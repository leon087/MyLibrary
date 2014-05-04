package wd.android.framework.ui;

import wd.android.framework.ui.BaseActivity.KeyEventListener;
import wd.android.util.util.MyLog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.MyDialogFragment;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseDialogFragment extends MyDialogFragment implements
		IFragment, KeyEventListener {

	protected BaseActivity mActivity;

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

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// 该方法先于onStart执行，在tab切换时会触发
		super.setUserVisibleHint(isVisibleToUser);
		MyLog.i("isVisibleToUser = " + isVisibleToUser + ",isVisible() = "
				+ isVisible());
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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}
}