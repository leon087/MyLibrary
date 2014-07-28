package cm.android.custom.http;

import cm.android.common.http.CacheHttpListener;
import cm.android.custom.MyManager;
import cm.android.framework.ext.global.CommonTag;
import cm.android.util.MapUtil;
import cm.android.util.Utils;
import com.alibaba.fastjson.JSON;
import org.apache.http.Header;

import java.util.Map;

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
