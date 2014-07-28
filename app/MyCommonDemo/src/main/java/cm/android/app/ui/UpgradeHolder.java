package cm.android.app.ui;

import android.support.v4.app.DialogFragment;
import cm.android.app.global.Tag;
import cm.android.app.manager.UpgradeManager;
import cm.android.app.ui.fragment.dialog.ConfirmDialog;
import cm.android.app.ui.fragment.dialog.UpgradeDialog;
import cm.android.cmcommondemo.R;
import cm.android.custom.MainApp;
import cm.android.custom.MyManager;
import cm.android.framework.ext.ui.BaseActivity;
import cm.android.sdk.MyHandler;
import cm.android.util.MapUtil;

import java.util.Map;

public class UpgradeHolder {
	private UpgradeManager upgradeManager = null;

	/**
	 * 确认是否升级对话框
	 */
	private ConfirmDialog confirmDialog = null;
	/**
	 * 正在下载对话框
	 */
	private UpgradeDialog upgradeDialog = null;

	private BaseActivity activity;
	private IUpgrade iUpgrade;

	public UpgradeHolder(BaseActivity activity, IUpgrade iUpgrade) {
		this.activity = activity;
		this.iUpgrade = iUpgrade;
		upgradeManager = new UpgradeManager(uiHandler.getHandler());
	}

	public void check() {
		// //
		// http://115.29.190.0:8080/q.jsp?CD=03000302-99031-302000050090000&CC=40004-10A001&CV=2.0.0.5&UA=huawei_y500-t00_android&SK=15
		// // upgradeManager.checkSoftUpadte();
		// // String qUrl = "http://115.29.190.0:8080/q.jsp";
		// String qUrl = "";
		// RequestParams requestParams = new RequestParams();
		// requestParams.add(Tag.CD, "03000302-99031-302000050090000");
		// requestParams.add(Tag.CC, "40004-10A001");
		// requestParams.add(Tag.CV, DeviceManager.getVersionName());
		// requestParams.add(Tag.UA, DeviceManager.getUserAgent());
		// requestParams.add(Tag.SK, String.valueOf(Build.VERSION.SDK_INT));
		// String url = AsyncHttpClient.getUrlWithQueryString(true, qUrl,
		// requestParams);
		// HttpUtil.exec(url, new BaseHttpListener() {
		// @Override
		// protected void onSuccess(Map<String, String> headers,
		// Map<String, Object> responseMap) {
		// int type = MapUtil.getInt(responseMap, Tag.CODE);
		// upgradeManager.checkUpgrade(responseMap);
		// }
		// });
		Map<String, Object> upgradeInfo = MyManager.getData(Tag.UPGRADE_INFO);
		int type = MapUtil.getInt(upgradeInfo, Tag.UPGRADE_TYPE);
		int versionCode = MapUtil.getInt(upgradeInfo, Tag.VERSION_CODE);
		upgradeManager.checkUpgrade(type, versionCode);
	}

	private ConfirmDialog.IConfirm iConfirm = new ConfirmDialog.IConfirm() {
		@Override
		public void onPositive() {
			// 升级
			showDownloadDialog();
			Map<String, Object> upgradeInfo = MyManager
					.getData(Tag.UPGRADE_INFO);
			String url = MapUtil.getString(upgradeInfo, Tag.UPGRADE_URL);
			upgradeManager.upgrade(url);
		}

		@Override
		public void onNegative() {
			// 不升级 启动程序
			ungradeCanceled();
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
			upgradeDialog.setCancelable(false);
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

	private MyHandler uiHandler = new MyHandler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UpgradeManager.MSG_UPGRADE_NONE:
				// 不需要升级
				ungradeCanceled();
				break;
			case UpgradeManager.MSG_UPGRADE:
				showConfirmDialog();
				break;
			case UpgradeManager.MSG_FAILED:
				upgradeFailed();
				break;
			case UpgradeManager.MSG_INSTALLING:
				dismissDialog();
				showConfirmDialog();
				break;
			default:
				break;
			}
		}
	};

	private void dismissDialog() {
		dismissDialog(upgradeDialog);
		dismissDialog(confirmDialog);
	}

	private void upgradeFailed() {
		dismissDialog(upgradeDialog);
		showConfirmDialog();
	}

	private void ungradeCanceled() {
		dismissDialog();
		if (iUpgrade != null) {
			iUpgrade.cancel();
		}
	}

	public static interface IUpgrade {
		void cancel();
	}
}
