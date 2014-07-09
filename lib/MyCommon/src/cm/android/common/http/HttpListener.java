package cm.android.common.http;

import cm.android.util.util.Utils;
import org.apache.http.Header;

import java.util.Map;

public abstract class HttpListener<T> {

	protected void onFinish() {
	}

	protected void onStart() {
	}

	protected void onFailure(Throwable error, T responseMap) {
	}

	protected abstract void onSuccess(int statusCode,
			Map<String, String> headers, T responseMap);

	void onSuccess(int statusCode, Header[] headers, byte[] responseBytes,
			T responseMap) {
		Map<String, String> headMap = Utils.genHeaderMap(headers);
		onSuccess(statusCode, headMap, responseMap);
	}

	protected abstract T parseResponse(Header[] headers, byte[] responseBytes)
			throws Throwable;

}
