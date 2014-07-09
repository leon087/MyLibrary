package cm.android.app.ui;

import android.os.Build;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import cm.android.app.global.Tag;
import cm.android.app.manager.DeviceManager;
import cm.android.app.manager.UpgradeManager;
import cm.android.app.ui.fragment.dialog.ConfirmDialog;
import cm.android.app.ui.fragment.dialog.UpgradeDialog;
import cm.android.common.http.HttpUtil;
import cm.android.custom.MainApp;
import cm.android.custom.http.BaseHttpListener;
import cm.android.framework.ui.BaseActivity;
import cm.android.wdcommondapi.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.Map;

public class UpgradeHolder {
	private UpgradeManager upgradeManager = null;
	/**
	 * 升级类型
	 */
	private int updateType;

	/**
	 * 确认是否升级对话框
	 */
	private ConfirmDialog confirmDialog = null;
	/**
	 * 正在下载对话框
	 */
	private UpgradeDialog upgradeDialog = null;

	private BaseActivity activity;

	public UpgradeHolder(BaseActivity activity) {
		this.activity = activity;
		upgradeManager = new UpgradeManager(softUpdateHandler);
	}

	public void check() {
		// http://115.29.190.0:8080/q.jsp?CD=03000302-99031-302000050090000&CC=40004-10A001&CV=2.0.0.5&UA=huawei_y500-t00_android&SK=15
		// upgradeManager.checkSoftUpadte();
		// String qUrl = "http://115.29.190.0:8080/q.jsp";
		String qUrl = "";
		RequestParams requestParams = new RequestParams();
		requestParams.add(Tag.CD, "03000302-99031-302000050090000");
		requestParams.add(Tag.CC, "40004-10A001");
		requestParams.add(Tag.CV, DeviceManager.getVersionName());
		requestParams.add(Tag.UA, DeviceManager.getUserAgent());
		requestParams.add(Tag.SK, String.valueOf(Build.VERSION.SDK_INT));
		String url = AsyncHttpClient.getUrlWithQueryString(true, qUrl,
				requestParams);
		HttpUtil.exec(url, new BaseHttpListener<Map<String, Object>>() {
			@Override
			protected void onSuccess(Map<String, String> headers,
					Map<String, Object> responseMap) {
				upgradeManager.checkSoftUpadte(responseMap);
			}
		});
	}

	private ConfirmDialog.IConfirm iConfirm = new ConfirmDialog.IConfirm() {
		@Override
		public void onPositive() {
			// 升级
			showDownloadDialog();
			upgradeManager.upgrade();
		}

		@Override
		public void onNegative() {
			// 不升级 启动程序
			upgradeComplete();
		}
	};

	/**
	 * 升级下载软件包对话框
	 */
	private void showDownloadDialog() {
		// if (upgradeDialog == null) {
		// upgradeDialog = new UpgradeDialog();
		// }
		// mFragmentHelper.showDialog(null, upgradeDialog);
		if (null == upgradeDialog) {
			String title = MainApp.getApp().getString(
					R.string.upgrade_download_title);
			String content = MainApp.getApp().getString(
					R.string.upgrade_download_content);
			upgradeDialog = UpgradeDialog.newInstance(title, content);
		}
		activity.showDialog(null, upgradeDialog);
	}

	private void dismissDialog(DialogFragment dialogFragment) {
		if (dialogFragment != null) {
			dialogFragment.dismiss();
		}
	}

	/**
	 * 软件版本升级对话框,用户选择升级或不升级
	 */
	private void showConfirmDialog() {
		if (confirmDialog == null) {
			String title = MainApp.getApp().getString(
					R.string.upgrade_confirm_title);
			String content = MainApp.getApp().getString(
					R.string.upgrade_confirm_content);
			confirmDialog = ConfirmDialog.newInstance(title, content, false);
			confirmDialog.setOnClickListener(iConfirm);
			confirmDialog.setCancelable(false);
		}
		activity.showDialog(null, confirmDialog);
	}

	private Handler softUpdateHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UpgradeManager.TYPE_DIALOG_UPGRADE:
				updateType = (Integer) msg.obj;
				if (UpgradeManager.UPGRADE_NONEED == updateType) {
					upgradeComplete();
				} else {
					showConfirmDialog();
				}
				break;
			case UpgradeManager.TYPE_DIALOG_FAILED:
				upgradeFailed();
				break;
			case UpgradeManager.TYPE_DIALOG_INSTALL:
				upgradeComplete();
				break;
			default:
				break;
			}
		}
	};

	private void upgradeComplete() {
		dismissDialog(upgradeDialog);
		dismissDialog(confirmDialog);
	}

	private void upgradeFailed() {
		dismissDialog(upgradeDialog);
		showConfirmDialog();
	}
}
