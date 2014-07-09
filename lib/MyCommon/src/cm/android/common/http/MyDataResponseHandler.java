package cm.android.common.http;

import cm.android.util.util.MyLog;
import cm.android.util.util.Utils;
import org.apache.http.Header;

public class MyDataResponseHandler<T> extends HttpResponseHandler<T> {
	private String url;
	private HttpListener<T> httpListener;

	public MyDataResponseHandler(String url, HttpListener<T> httpListener) {
		this.url = url;
		this.httpListener = httpListener;
	}

	@Override
	public void onFinish() {
		super.onFinish();
		httpListener.onFinish();
		if (MyLog.isDebug()) {
			MyLog.d("url = " + url);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		httpListener.onStart();
		if (MyLog.isDebug()) {
			MyLog.d("url = " + url);
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers,
			byte[] responseBytes, T response) {
		MyLog.i("statusCode = " + statusCode);
		if (MyLog.isDebug()) {
			MyLog.d("header:----------------------------------------");
			for (Header header : headers) {
				MyLog.d(header.getName() + ":" + header.getValue());
			}
			MyLog.d("responseBody:----------------------------------------");
			String responseBody = Utils.getString(responseBytes, getCharset());
			MyLog.d(responseBody);
		}

		httpListener.onSuccess(statusCode, headers, responseBytes, response);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			Throwable throwable, byte[] responseBytes, T errorResponse) {
		httpListener.onFailure(throwable, errorResponse);
		String responseBody = Utils.getString(responseBytes, getCharset());
		MyLog.e(responseBody, throwable);
	}

	@Override
	protected T parseResponse(Header[] headers, byte[] responseBytes,
			boolean isFailure) throws Throwable {
		return httpListener.parseResponse(headers, responseBytes);
	}
}
