package wd.android.app.ui.activity;

import java.util.Map;

import wd.android.app.global.Tag;
import wd.android.app.ui.MyUIUtil;
import wd.android.app.ui.fragment.dialog.LoadingDialog;
import wd.android.custom.MainApp;
import wd.android.custom.http.BaseHttpListener;
import wd.android.framework.ui.BaseActivity;
import wd.android.util.util.MapUtil;
import wd.android.util.util.Utils;
import wd.android.wdcommondapi.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public abstract class MyBaseActivity extends BaseActivity {

	public static final int MSG_SHOW_PROGRESS = 0x01;
	public static final int MSG_DISMISS_PROGRESS = 0x02;
	private LoadingDialog mLoadingDialog;

	@Override
	public void onBeforeContentView(Bundle savedInstanceState) {
		super.onBeforeContentView(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void onCreateActivity(Bundle savedInstanceState) {
		// if (savedInstanceState != null) {
		// // 恢复时结束进程重新打开
		// ((MainApp) MainApp.getApp()).restartApp(this);
		// finish();
		// return;
		// }
		super.onCreateActivity(savedInstanceState);
	}

	@Override
	public void onDestroyActivity() {
		super.onDestroyActivity();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public abstract class MyBaseHttpHandler extends BaseHttpListener {
		// @Override
		// protected void onSuccess(Map<String, String> headers,
		// Map<String, Object> responseMap) {
		// }

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
			showLoadingHandler.sendEmptyMessage(MSG_SHOW_PROGRESS);
		}

		@Override
		public void onFinish() {
			showLoadingHandler.sendEmptyMessage(MSG_DISMISS_PROGRESS);
		}
	}

	public Handler showLoadingHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_PROGRESS:
				showLoadingDialog();
				break;
			case MSG_DISMISS_PROGRESS:
				dismissLoadingDialog();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * show loading dialog
	 */
	private void showLoadingDialog() {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog();
		}
		mFragmentHelper.showDialog(null, mLoadingDialog);
	}

	/**
	 * dismiss loading show
	 */
	private void dismissLoadingDialog() {
		if (mLoadingDialog != null) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}

}
