package wd.android.custom.http;

import java.util.Map;

import wd.android.common.http.CacheHttpListener;
import wd.android.custom.MyManager;
import wd.android.framework.global.CommonTag;
import wd.android.util.util.MapUtil;
import wd.android.util.util.Utils;

public abstract class BaseHttpListener extends CacheHttpListener {

	@Override
	protected final void onSuccess(int statusCode, Map<String, String> headers,
			Map<String, Object> responseMap) {
		String sessionId = MapUtil.getString(headers, CommonTag.SESSION_ID);
		if (!Utils.isEmpty(sessionId)) {
			// String sessionId = getSessionId(setCookie);
			MyManager.getGlobalData().putData(CommonTag.SESSION_ID, sessionId);
		}

		onSuccess(headers, responseMap);
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// protected Map<String, Object> parseResponse(Header[] headers,
	// byte[] responseBytes) throws Throwable {
	// byte[] base64 = Base64.decode(responseBytes, Base64.DEFAULT);
	// byte[] realByte = EncryptUtils.decrypt(base64);
	// String rawJsonData = Utils.getString(realByte,
	// AsyncHttpResponseHandler.DEFAULT_CHARSET);
	// return JSON.parseObject(rawJsonData, Map.class);
	// }

	// private String getSessionId(String setCookie) {
	// return (setCookie.split(";")[0]).split("=")[1];
	// }

	protected abstract void onSuccess(Map<String, String> headers,
			Map<String, Object> responseMap);
}
