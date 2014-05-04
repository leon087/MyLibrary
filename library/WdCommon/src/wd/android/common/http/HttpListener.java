package wd.android.common.http;

import java.util.Map;

import org.apache.http.Header;

import wd.android.util.util.Utils;

public abstract class HttpListener {

	protected void onFinish() {
	}

	protected void onStart() {
	}

	protected void onFailure(Throwable error, Map<String, Object> responseMap) {
	}

	protected abstract void onSuccess(int statusCode,
			Map<String, String> headers, Map<String, Object> responseMap);

	void onSuccess(int statusCode, Header[] headers, byte[] responseBytes,
			Map<String, Object> responseMap) {
		Map<String, String> headMap = Utils.genHeaderMap(headers);
		onSuccess(statusCode, headMap, responseMap);
	}

	protected abstract Map<String, Object> parseResponse(Header[] headers,
			byte[] responseBytes) throws Throwable;

}
