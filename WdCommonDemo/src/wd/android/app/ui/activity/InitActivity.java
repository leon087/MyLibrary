package wd.android.app.ui.activity;

import wd.android.app.global.Tag;
import wd.android.app.manager.InitManager;
import wd.android.app.manager.InitManager.OnInit;
import wd.android.app.ui.MyUIUtil;
import wd.android.app.ui.UpgradeHolder;
import wd.android.app.ui.UpgradeHolder.IUpgrade;
import wd.android.app.ui.fragment.dialog.ConfirmDialog;
import wd.android.custom.MainApp;
import wd.android.custom.MyManager;
import wd.android.framework.util.MyIntent;
import wd.android.util.net.NetworkUtil;
import wd.android.util.sdk.MyHandler;
import wd.android.util.util.UIUtils;
import wd.android.util.util.Utils;
import wd.android.wdcommondemo.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

public class InitActivity extends MyBaseActivity {
	public static final int CODE_EXIT = Activity.RESULT_CANCELED;
	public static final int CODE_RESTART = CODE_EXIT + 1;

	// private TextView initTips;
	private ImageView initView;
	private InitManager initManager;

	/**
	 * 显示loading对话框
	 */
	public static final int MSG_SHOW_PROGRESS = 0x01;
	/**
	 * 关闭loading对话框
	 */
	public static final int MSG_DISMISS_PROGRESS = 0x02;
	/**
	 * 设置loading对话框
	 */
	public static final int MSG_SET_PROGRESS = 0x03;

	/**
	 * 初始化，获取初始化数据
	 */
	public static final int OPERATE_INIT = 0x01;
	/**
	 * 检查更新
	 */
	public static final int OPERATE_UPGRADE = OPERATE_INIT + 1;
	/**
	 * 跳转到首页
	 */
	public static final int OPERATE_HOME = OPERATE_INIT + 3;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 从首页返回则退出
		if (requestCode == CODE_EXIT) {
			MainApp.getApp().exitApp();
		}
	}

	/**
	 * UI处理handler
	 */
	private MyHandler uiHandler = new MyHandler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_PROGRESS:
				// mProgressingDialog.show();
				break;
			case MSG_DISMISS_PROGRESS:
				// mProgressingDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void initView(View rootView, Bundle bundle) {
		// initTips = UIUtils.findView(rootView, R.id.init_tips);
		initView = UIUtils.findView(rootView, R.id.init_imgview_bg);
	}

	@Override
	public void initData(Bundle bundle) {
		sendOperate(OPERATE_INIT);
	}

	@Override
	public int getRootViewId() {
		return R.layout.p_activity_init;
	}

	private void sendOperate(int what) {
		initHandler.obtainMessage(what).sendToTarget();
	}

	private void toHomePage() {
		MyIntent.startActivityForResult(InitActivity.this, HomeActivity.class,
				null, Activity.RESULT_CANCELED);
		// 通过clearTop的方式返回到InitAcitvity，让系统自动清除Activity栈
	}

	public void onInitFailed(String errorMsg) {
		if (Utils.isEmpty(errorMsg)) {
			errorMsg = this.getString(R.string.http_exception);
		}
		MyUIUtil.showToast(errorMsg);
		// uiHandler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// MainApp.getApp().exitApp();
		// }
		// }, 1000);

		// 弹对话框
		OnFailedHodler onFailedHodler = new OnFailedHodler();
		onFailedHodler.process();
	}

	private void init() {
		// MainApp.getApp().initApp();
		initManager = new InitManager(InitActivity.this, new OnInit() {
			@Override
			public void onPreInit() {

			}

			@Override
			public void onInitSucceed() {
				sendOperate(OPERATE_UPGRADE);
			}

			@Override
			public void onInitFailed(String errorMsg) {
				InitActivity.this.onInitFailed(errorMsg);
			}

			@Override
			public void onInit() {

			}
		});
	}

	/**
	 * 初始化流程handler
	 */
	private MyHandler initHandler = new MyHandler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case OPERATE_INIT:
				// 网络检测中
				setText(R.string.app_init);
				setImage();
				init();
				// 如果网络连接可用，进行连接
				// 如果网络连接不可用，直接弹出错误提示
				// if (NetworkUtil.isConnected(InitActivity.this)) {
				int type = NetworkUtil.getNetWorkAvailable(InitActivity.this);
				if (type != -1000) {
					// 发送load请求
					initManager.request();
				} else {
					// 无网络链接
					setText(R.string.net_unavailable);
					onInitFailed(getString(R.string.net_unavailable));
				}
				break;
			case OPERATE_UPGRADE:
				UpgradeHolder upgradeHolder = new UpgradeHolder(
						InitActivity.this, new IUpgrade() {
							@Override
							public void cancel() {
								sendOperate(OPERATE_HOME);
							}
						});
				upgradeHolder.check();
				break;
			case OPERATE_HOME:
				// 跳转到首页
				toHomePage();
				break;
			default:
				break;
			}
		}
	};

	private void setText(int tipId) {
		String tips = MainApp.getApp().getString(tipId);
		// initTips.setText(tips);
	}

	private void setImage() {
		String imgUrl = MyManager.getMyPreference().read(Tag.LOADING_IMAGE, "");
		if (!Utils.isEmpty(imgUrl)) {
			MyManager.getAsyncImageManager().loadImage(imgUrl, initView,
					R.drawable.welcome_bg);
		}
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}

	private class OnFailedHodler {

		/**
		 * 确认是否升级对话框
		 */
		private ConfirmDialog onFailedDialog = null;

		private ConfirmDialog.IConfirm iConfirm = new ConfirmDialog.IConfirm() {
			@Override
			public void onPositive() {
				sendOperate(OPERATE_INIT);
				dismissDialog(onFailedDialog);
			}

			@Override
			public void onNegative() {
				MainApp.getApp().exitApp();
			}
		};

		public void process() {
			showConfirmDialog();
		}

		public void release() {
			dismissDialog(onFailedDialog);
		}

		private void showConfirmDialog() {
			if (onFailedDialog == null) {
				String title = MainApp.getApp().getString(R.string.confirm);
				String content = MainApp.getApp().getString(
						R.string.http_exception);
				onFailedDialog = ConfirmDialog.newInstance(title, content,
						getString(R.string.retry), getString(R.string.exit));
				onFailedDialog.setOnClickListener(iConfirm);
				onFailedDialog.setCancelable(false);
			}
			showDialog(null, onFailedDialog);
		}

		private void dismissDialog(DialogFragment dialogFragment) {
			if (dialogFragment != null) {
				dialogFragment.dismiss();
			}
		}
	}
}
