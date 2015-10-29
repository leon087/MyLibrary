package cm.android.common.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.util.Patterns;

import java.util.Map;

import cm.java.util.ObjectUtil;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public final class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger("HTTP");

    private final static AsyncHttpClient client = new AsyncHttpClient();

    private HttpUtil() {
    }

    static {
        HttpConfig.initConfig(client);
    }

    public static void setURLEncodingEnabled(boolean enable) {
        client.setURLEncodingEnabled(enable);
    }

//    public static void cancel(Context context) {
//        client.cancelRequests(context, true);
//    }

    public static void cancel(String tag) {
        client.cancelRequestsByTAG(tag, true);
    }

    public static void cancelAll() {
        client.cancelAllRequests(true);
    }

    public static <T> void exec(Context context, String tag, String url, Header[] headers,
            RequestParams params, HttpListener<T> httpListener) {
        MyDataResponseHandler<T> responseHandler = new MyDataResponseHandler<T>(httpListener);
        exec(context, tag, url, headers, params, responseHandler);
    }

    public static void exec(Context context, String tag, String url, Header[] headers,
            RequestParams params, AsyncHttpResponseHandler responseHandler) {
        responseHandler.setTag(tag);

        logger.info("RequestParams = " + params);
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            logger.error("url = " + url);
            responseHandler.onFailure(0, null, null, new IllegalArgumentException("url = " + url));
            return;
        }

        // Header[] headers = HttpUtil.genHeader(headerMap);
        if (params == null) {
            client.get(context, url, headers, null, responseHandler);
        } else {
            client.post(context, url, headers, params, null, responseHandler);
        }
    }

//    public static <T> void exec(Context context, String url, Header[] header,
//            byte[] data, HttpListener<T> httpListener) {
//        MyDataResponseHandler<T> responseHandler = new MyDataResponseHandler<T>(httpListener);
//        exec(context, url, header, data, responseHandler);
//    }
//
//    public static void exec(Context context, String url, Header[] header,
//            byte[] data, AsyncHttpResponseHandler responseHandler) {
//        if (!Patterns.WEB_URL.matcher(url).matches()) {
//            logger.error("url = " + url);
//            responseHandler.onFailure(0, null, null, new IllegalArgumentException("url = " + url));
//            return;
//        }
//
//        if (null == data) {
//            client.post(context, url, header, (HttpEntity) null, null, responseHandler);
//        } else {
//            ByteArrayEntity entity = new ByteArrayEntity(data);
//            client.post(context, url, header, entity, null, responseHandler);
//        }
//    }

    public static <T> void exec(Context context, String tag, String url, Header[] header,
            byte[] data, HttpListener<T> httpListener) {
        MyDataResponseHandler<T> responseHandler = new MyDataResponseHandler<T>(httpListener);
        exec(context, tag, url, header, data, responseHandler);
    }

    public static void exec(Context context, String tag, String url, Header[] header, byte[] data,
            AsyncHttpResponseHandler responseHandler) {
        responseHandler.setTag(tag);

        if (!Patterns.WEB_URL.matcher(url).matches()) {
            logger.error("url = " + url);
            responseHandler.onFailure(0, null, null, new IllegalArgumentException("url = " + url));
            return;
        }

        if (null == data) {
            client.post(context, url, header, (HttpEntity) null, null, responseHandler);
        } else {
            ByteArrayEntity entity = new ByteArrayEntity(data);
            client.post(context, url, header, entity, null, responseHandler);
        }
    }

    /**
     * 设置Cookie，之后从Server获取到的cookies将被保存至PersistentCookieStore
     */
    public static void setCookieStore(Context context) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        setCookieStore(myCookieStore);
    }

    public static void setCookieStore(CookieStore cookieStore) {
        client.setCookieStore(cookieStore);
    }

    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }

    public static void addHeader(String header, String value) {
        client.addHeader(header, value);
    }

    public static void removeHeader(String header) {
        client.removeHeader(header);
    }

    public static void setProxy(String hostname, int port) {
        client.setProxy(hostname, port);
    }

    /**
     * 配置HttpClient
     */
    public static class HttpConfig {

        private static final int DEFAULT_MAX_CONNECTIONS = 3;

        private static final int DEFAULT_TIMEOUT = 15 * 1000;

        private static final int DEFAULT_MAX_RETRIES = 2;

        private static final int DEFAULT_RETRY_SLEEP_TIME_MILLIS = 200;

        /**
         * 初始化HttpClient配置信息
         */
        public static void initConfig(AsyncHttpClient asyncHttpClient) {
            // 设置线程
            // asyncHttpClient.setThreadPool((ThreadPoolExecutor) Executors
            // .newFixedThreadPool(DEFAULT_MAX_CONNECTIONS));
            asyncHttpClient.setMaxConnections(DEFAULT_MAX_CONNECTIONS);
            asyncHttpClient.setTimeout(DEFAULT_TIMEOUT);
            asyncHttpClient.setMaxRetriesAndTimeout(DEFAULT_MAX_RETRIES,
                    DEFAULT_RETRY_SLEEP_TIME_MILLIS);

            // final HttpParams httpParams = httpClient.getParams();
            //
            // ConnManagerParams.setTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
            // ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
            // new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
            // ConnManagerParams.setMaxTotalConnections(httpParams,
            // DEFAULT_MAX_CONNECTIONS);
            //
            // HttpConnectionParams.setSoTimeout(httpParams,
            // DEFAULT_SOCKET_TIMEOUT);
            // HttpConnectionParams.setConnectionTimeout(httpParams,
            // DEFAULT_SOCKET_TIMEOUT);
            // HttpConnectionParams.setTcpNoDelay(httpParams, true);
            // HttpConnectionParams.setSocketBufferSize(httpParams,
            // DEFAULT_SOCKET_BUFFER_SIZE);

            // httpClient.setHttpRequestRetryHandler(new
            // RetryHandler(DEFAULT_MAX_RETRIES));
        }
    }

    public static Map<String, String> genHeaderMap(Header[] headers) {
        Map<String, String> headMap = ObjectUtil.newHashMap();
        for (Header header : headers) {
            headMap.put(header.getName(), header.getValue());
        }
        return headMap;
    }

    public static String getHeader(Header[] headers, String headerName) {
        if (headers == null) {
            return "";
        }

        for (Header header : headers) {
            if (headerName.equals(header.getName())) {
                return header.getValue();
            }
        }

        return "";
    }
}
