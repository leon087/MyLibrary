package wd.android.custom.http;

import wd.android.app.global.UrlData;
import wd.android.app.manager.DeviceManager;
import wd.android.common.http.HttpUtil;
import wd.android.common.http.MyDataResponseHandler;
import wd.android.util.util.Utils;
import android.util.Base64;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uus.mv2.uitl.EncryptUtils;

public class MyHttpUtil {
	public static void exec(String linkUrl, BaseHttpListener httpListener) {
		exec(linkUrl, null, httpListener);
	}

	public static String encryptURL(String url) {
		byte[] encryptParas = EncryptUtils.encrypt(url);
		String base64encode = Base64.encodeToString(encryptParas,
				Base64.DEFAULT);
		base64encode = "body=" + base64encode;
		return base64encode;
	}

	public static String decode(byte[] responseBytes) {
		byte[] base64 = Base64.decode(responseBytes, Base64.DEFAULT);
		byte[] realByte = EncryptUtils.decrypt(base64);
		String rawJsonData = Utils.getString(realByte,
				AsyncHttpResponseHandler.DEFAULT_CHARSET);
		return rawJsonData;
	}

	// private static Map<String, String> genHeader() {
	// Map<String, String> params = ObjectUtil.newHashMap();
	// params.put("test", "test");
	// return params;
	// }

	private static RequestParams genHeader() {
		RequestParams params = new RequestParams();
		params.put("test", "test");
		return params;
	}

	public static void exec(String linkUrl, RequestParams params,
			BaseHttpListener httpListener) {
		RequestParams header = genHeader();

		String encryptHeaderUrl = AsyncHttpClient.getUrlWithQueryString(true,
				linkUrl, header);
		String encryptUrl = AsyncHttpClient.getUrlWithQueryString(true,
				encryptHeaderUrl, params);
		String encryptBytes = encryptURL(encryptUrl);

		RequestParams urlParams = new RequestParams();
		urlParams.put("packagename", DeviceManager.getPackageName());
		urlParams.put("versionName", DeviceManager.getVersionName());
		String url = AsyncHttpClient.getUrlWithQueryString(true,
				UrlData.URL_INIT_ENCRYPT, urlParams);

		MyDataResponseHandler responseHandler = new MyDataResponseHandler(url,
				httpListener);
		HttpUtil.exec(url, encryptBytes.getBytes(), responseHandler);
	}
}
