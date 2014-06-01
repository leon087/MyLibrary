package wd.android.custom.http;

import java.util.Map;

import org.apache.http.Header;

import wd.android.common.http.CacheHttpListener;
import wd.android.custom.MyManager;
import wd.android.framework.global.CommonTag;
import wd.android.util.util.MapUtil;
import wd.android.util.util.Utils;

import com.alibaba.fastjson.JSON;

public abstract class BaseHttpListener<T> extends CacheHttpListener<T> {

	public BaseHttpListener() {
		super();
	}

	@Override
	protected final void onSuccess(int statusCode, Map<String, String> headers,
			T responseMap) {
		String sessionId = MapUtil.getString(headers, CommonTag.SESSION_ID);
		if (!Utils.isEmpty(sessionId)) {
			// String sessionId = getSessionId(setCookie);
			MyManager.getGlobalData().putData(CommonTag.SESSION_ID, sessionId);
		}

		onSuccess(headers, responseMap);
	}

	@Override
	protected T parseResponse(Header[] headers, byte[] responseBytes)
			throws Throwable {
		String rawJsonData = MyHttpUtil.decode(responseBytes);
		return JSON.parseObject(rawJsonData, clazz);
	}

	protected abstract void onSuccess(Map<String, String> headers, T responseMap);
}
