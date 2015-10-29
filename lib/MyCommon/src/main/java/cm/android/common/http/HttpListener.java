package cm.android.common.http;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

public abstract class HttpListener<T> {

    protected void onFinish() {
    }

    protected void onStart() {
    }

    protected void onFailure(Throwable error, byte[] responseBytes, T responseMap) {
    }

    protected abstract void onSuccess(int statusCode,
            Map<String, String> headers, byte[] responseBytes, T responseMap);

    void onSuccess(int statusCode, Header[] headers, byte[] responseBytes,
            T responseMap) {
        Map<String, String> headMap = HttpUtil.genHeaderMap(headers);
        onSuccess(statusCode, headMap, responseBytes, responseMap);
    }

    protected abstract T parseResponse(Header[] headers, byte[] responseBytes)
            throws Throwable;

}
