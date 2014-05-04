package wd.android.app.manager;

import java.io.File;

import org.apache.http.Header;

import wd.android.common.http.HttpUtil;
import wd.android.framework.BaseApp;
import wd.android.util.util.EnvironmentInfo;
import wd.android.util.util.IntentUtil;
import wd.android.util.util.MyLog;
import wd.android.util.util.Utils;
import android.net.Uri;
import android.os.Handler;

import com.loopj.android.http.ApkHttpResponseHandler;

public class UpgradeManager {
	public static enum UpgradeType {
		/**
		 * 不升级
		 */
		UPGRADE_NONE(-1),
		/**
		 * 升级
		 */
		UPGRADE(3);

		private int type;

		private UpgradeType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}
	}

	/**
	 * 不升级
	 */
	public static final int MSG_UPGRADE_NONE = 0;
	/** 确认升级对话框 */
	public static final int MSG_UPGRADE = MSG_UPGRADE_NONE + 1;
	/** 正在下载对话框 */
	public static final int MSG_DOWNLOADING = MSG_UPGRADE_NONE + 2;
	/** 失败对话框 */
	public static final int MSG_FAILED = MSG_UPGRADE_NONE + 3;
	/** 正在安装 */
	public static final int MSG_INSTALLING = MSG_UPGRADE_NONE + 4;

	/** 软件版本升级handler实例 */
	private Handler uiHandler = null;

	private static final String APK_LOCAL_NAME = "update.apk";

	/**
	 * 软件版本升级管理构造方法
	 * 
	 * @param context
	 * @param upgradeHandler
	 */
	public UpgradeManager(Handler softUpdateHandler) {
		this.uiHandler = softUpdateHandler;
	}

	public void checkUpgrade(int type, int versionCode) {
		if (UpgradeType.UPGRADE_NONE.getType() == type) {
			sendMsg(MSG_UPGRADE_NONE);
			return;
		}

		if (versionCode > DeviceManager.getVersionCode()) {
			// 升级
			sendMsg(MSG_UPGRADE);
		} else {
			sendMsg(MSG_UPGRADE_NONE);
		}
	}

	private void sendMsg(int what) {
		uiHandler.obtainMessage(what).sendToTarget();
	}

	/**
	 * 执行升级
	 */
	public void upgrade(String upgradeUrl) {
		if (checkSpace()) {
			downloadApk(upgradeUrl);
		}
	}

	/**
	 * 下载apk包的请求
	 * 
	 * @param updateUrl
	 */
	private void downloadApk(String upgradeUrl) {
		MyLog.i("upgradeUrl = " + upgradeUrl);
		if (Utils.isEmpty(upgradeUrl)) {
			// TODO
			sendMsg(MSG_FAILED);
		} else {
			// // 下载
			ApkHttpResponseHandler apkResponseHandler = new ApkHttpResponseHandler(
					BaseApp.getApp(), APK_LOCAL_NAME) {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						File file) {
					sendMsg(MSG_INSTALLING);
					IntentUtil.installPackage(BaseApp.getApp(),
							Uri.fromFile(file));
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, File file) {
					sendMsg(MSG_FAILED);
				}

				@Override
				public void onProgress(int bytesWritten, int totalSize) {
					// super.onProgress(bytesWritten, totalSize);
					// uiHandler.sendMessage(uiHandler.obtainMessage(
					// TYPE_DIALOG_SOFTDOAN, percentMap));
				}
			};
			apkResponseHandler.deleteTargetFile();
			HttpUtil.exec(upgradeUrl, apkResponseHandler);
		}
	}

	/**
	 * 判断是否能够升级
	 * 
	 * @return
	 */
	private boolean checkSpace() {
		File file = EnvironmentInfo.getDataStorageDirectory(BaseApp.getApp());
		if (EnvironmentInfo.hasEnoughSpace(file)) {
			return true;
		} else {
			return false;
		}
	}
}
