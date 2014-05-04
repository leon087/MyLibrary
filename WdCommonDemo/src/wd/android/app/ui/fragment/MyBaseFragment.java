package wd.android.app.ui.fragment;

import java.util.Map;

import wd.android.app.global.Tag;
import wd.android.app.ui.MyUIUtil;
import wd.android.app.ui.activity.MyBaseActivity;
import wd.android.custom.MainApp;
import wd.android.custom.http.BaseHttpListener;
import wd.android.framework.ui.BaseFragment;
import wd.android.util.util.MapUtil;
import wd.android.util.util.Utils;
import wd.android.wdcommondemo.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class MyBaseFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		View fragmentView = super.onCreateView(inflater, container, bundle);
		if (fragmentView != null) {
			fragmentView.setOnClickListener(summyOnClickListener);
		}
		return fragmentView;
	}

	/**
	 * 屏蔽屏幕点击
	 */
	private static OnClickListener summyOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	public class MyBaseHttpHandler extends BaseHttpListener {
		@Override
		protected void onSuccess(Map<String, String> headers,
				Map<String, Object> responseMap) {
		}

		@Override
		protected void onFailure(Throwable error,
				Map<String, Object> responseMap) {
			super.onFailure(error, responseMap);
			String resultMsg = MapUtil.getString(responseMap, Tag.MSG);
			if (Utils.isEmpty(resultMsg)) {
				resultMsg = MainApp.getApp().getString(R.string.http_exception);
			}
			MyUIUtil.showToast(resultMsg);
		}

		@Override
		public void onStart() {
			((MyBaseActivity) mActivity).showLoadingHandler
					.sendEmptyMessage(MyBaseActivity.MSG_SHOW_PROGRESS);
		}

		@Override
		public void onFinish() {
			// super.onHttpResponse(task);
			((MyBaseActivity) mActivity).showLoadingHandler
					.sendEmptyMessage(MyBaseActivity.MSG_DISMISS_PROGRESS);
		}
	}
}
