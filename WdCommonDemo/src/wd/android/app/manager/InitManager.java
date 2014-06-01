package wd.android.app.manager;

import java.util.Map;

import wd.android.app.global.Tag;
import wd.android.app.global.UrlData;
import wd.android.common.http.HttpUtil;
import wd.android.custom.MyManager;
import wd.android.custom.http.BaseHttpListener;
import wd.android.util.util.MapUtil;
import wd.android.util.util.Utils;
import android.content.Context;

/**
 * 初始化管理类
 */
public class InitManager {
	/**
	 * 初始化接口
	 */
	public interface OnInit {
		/**
		 * 初始化之前执行
		 */
		void onPreInit();

		/**
		 * 正在初始化
		 */
		void onInit();

		/**
		 * 初始化成功
		 */
		void onInitSucceed();

		/**
		 * 初始化失败
		 * 
		 * @param errorMsg
		 */
		void onInitFailed(String errorMsg);
	}

	private OnInit iInitInterface;

	public InitManager(Context context, OnInit iInitInterface) {
		this.iInitInterface = iInitInterface;
	}

	private void addHeader() {
		// HttpUtil.addHeader(header, value);
	}

	/**
	 * 获取服务器下发的地址列表
	 */
	public void request() {
		// MainApp.getApp().initApp();
		// addHeader();

		iInitInterface.onPreInit();
		HttpUtil.exec(UrlData.URL_INIT, initHttpHandler);
	}

	private BaseHttpListener<Map<String, Object>> initHttpHandler = new BaseHttpListener<Map<String, Object>>() {
		@Override
		protected void onStart() {
			iInitInterface.onInit();
		};

		@Override
		protected void onSuccess(java.util.Map<String, String> headers,
				java.util.Map<String, Object> responseMap) {
			boolean flag = initLoadData(responseMap);
			if (flag) {
				iInitInterface.onInitSucceed();
			} else {
				iInitInterface.onInitFailed(null);
			}
		}

		@Override
		protected void onFailure(Throwable error,
				java.util.Map<String, Object> responseMap) {
			super.onFailure(error, responseMap);
			iInitInterface.onInitFailed(null);
		}
	};

	private boolean initLoadData(Map<String, Object> hashMap) {
		// 初始化地址列表
		Map<String, String> urls = MapUtil.getMap(hashMap, Tag.CONTENT);
		if (Utils.isEmpty(urls)) {
			return false;
		}
		UrlData urlData = new UrlData();
		urlData.initUrl(urls);
		MyManager.putData(Tag.URL_DATA, urlData);

		// upgradeInfo
		Map<String, Object> upgradeInfo = MapUtil.getMap(hashMap,
				Tag.UPGRADE_INFO);
		MyManager.putData(Tag.UPGRADE_INFO, upgradeInfo);
		// loadingInfo
		Map<String, String> loadingInfo = MapUtil.getMap(hashMap,
				Tag.LOADING_INFO);
		MyManager.putData(Tag.LOADING_INFO, loadingInfo);

		String imgUrl = MapUtil.getString(loadingInfo, Tag.IMG_URL);
		MyManager.getMyPreference().write(Tag.LOADING_IMAGE, imgUrl);

		return true;
	}
}
