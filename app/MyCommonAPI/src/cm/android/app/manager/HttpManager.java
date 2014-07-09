package cm.android.app.manager;

import cm.android.common.http.HttpUtil;

public class HttpManager {
	public static void initHeader() {
		HttpUtil.setUserAgent(DeviceManager.getUserAgent());

		HttpUtil.addHeader("Content-Type", "application/x-www-form-urlencoded");
		HttpUtil.addHeader("X_UP_CLIENT_ID", "000111");
		HttpUtil.addHeader("WDAccept-Encoding", "gzip,deflate");
		// 测试
		initHeaderTest();
	}

	public static void initHeaderTest() {
		// HttpUtil.addHeader("epgUserId", "228");
	}
}
