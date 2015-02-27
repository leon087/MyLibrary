package cm.android.common.http;

import org.apache.http.Header;

import java.util.Map;

import cm.java.util.Utils;

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
        Map<String, String> headMap = Utils.genHeaderMap(headers);
        onSuccess(statusCode, headMap, responseBytes, responseMap);
    }

    protected abstract T parseResponse(Header[] headers, byte[] responseBytes)
            throws Throwable;

}
