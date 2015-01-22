package cm.android.common.http.volley;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class MyHttp {

    private static RequestQueue requestQueue;

    private String tag;

    private boolean shouldCache;

    private static final Map<String, String> sHeaderMap = new HashMap<String, String>();

    public MyHttp(String tag) {
        this(tag, false);
    }

    public MyHttp(String tag, boolean shouldCache) {
        this.tag = tag;
        this.shouldCache = shouldCache;
    }

    public static void start(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.start();
    }

    public static void addHeader(String name, String value) {
        sHeaderMap.put(name, value);
    }

    public static void removeHeader(String name) {
        sHeaderMap.remove(name);
    }

    public static void stop() {
        if (requestQueue != null) {
            requestQueue.stop();
        }
    }

    public <T> void exec(String url, HttpListener<T> listener) {
        this.exec(url, sHeaderMap, (byte[]) null, listener);
    }

    public <T> void exec(String url, Map<String, String> headerMap,
            Map<String, String> bodyMap, HttpListener<T> listener) {
        DataRequest<T> request = new DataRequest<T>(url, bodyMap, listener);
        request.addHeader(headerMap);
        exec(request);
    }

    public <T> void exec(String url, Map<String, String> headerMap,
            byte[] body, HttpListener<T> listener) {
        DataRequest<T> request = new DataRequest<T>(url, body, listener);
        request.addHeader(headerMap);
        exec(request);
    }

    public <T> void exec(DataRequest<T> request) {
        request.addHeader(sHeaderMap);
        request.setTag(tag);
        request.setShouldCache(shouldCache);
        requestQueue.add(request);
    }

    public void cancel() {
        requestQueue.cancelAll(tag);
    }
}
