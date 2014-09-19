package cm.android.common.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger("HTTP");

    private final static AsyncHttpClient client = new AsyncHttpClient();

    private HttpUtil() {
    }

    static {
        HttpConfig.initConfig(client);
    }

    public static void cancel(Context context) {
        client.cancelRequests(context, true);
    }

    public static void cancelAll() {
        client.cancelAllRequests(true);
    }

    /**
     * JSON数据请求
     *
     * @param <T>
     */
    public static <T> void exec(String url, HttpListener<T> httpHandler) {
        exec(url, null, null, httpHandler);
    }

    /**
     * JSON数据请求
     *
     * @param <T>
     */
    public static <T> void exec(String url, Header[] headers,
                                HttpListener<T> httpHandler) {
        exec(url, headers, null, httpHandler);
    }

    /**
     * JSON数据请求
     *
     * @param <T>
     * @param <T>
     */
    public static <T> void exec(String url, RequestParams params,
                                HttpListener<T> httpHandler) {
        exec(url, null, params, httpHandler);
    }

    /**
     * JSON数据请求
     *
     * @param <T>
     */
    public static <T> void exec(String url, Header[] headers,
                                RequestParams params, HttpListener<T> httpListener) {
        MyDataResponseHandler<T> responseHandler = new MyDataResponseHandler<T>(httpListener);
        exec(url, headers, params, responseHandler);
    }

    public static void exec(String url, AsyncHttpResponseHandler responseHandler) {
        exec(url, null, (RequestParams) null, responseHandler);
    }

    public static void exec(String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        exec(url, null, params, responseHandler);
    }

    public static void exec(String url, Header[] headers, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {
        exec(null, url, headers, params, responseHandler);
    }

    public static void exec(Context context, String url, Header[] headers,
                            RequestParams params, AsyncHttpResponseHandler responseHandler) {
        logger.info("RequestParams = " + params);
        // Header[] headers = HttpUtil.genHeader(headerMap);
        if (params == null) {
            client.get(context, url, headers, null, responseHandler);
        } else {
            client.post(context, url, headers, params, null, responseHandler);
        }
    }

    public static <T> void exec(String url, byte[] b, HttpListener<T> httpListener) {
        MyDataResponseHandler<T> responseHandler = new MyDataResponseHandler<T>(httpListener);
        exec(url, b, responseHandler);
    }

    public static void exec(String url, byte[] b,
                            AsyncHttpResponseHandler responseHandler) {
        exec(null, url, null, b, responseHandler);
    }

    public static void exec(String url, Header[] header, byte[] b,
                            AsyncHttpResponseHandler responseHandler) {
        exec(null, url, header, b, responseHandler);
    }

    public static void exec(Context context, String url, Header[] header,
                            byte[] b, AsyncHttpResponseHandler responseHandler) {
        // client.post(null, url, header, RequestParams, String,
        // ResponseHandlerInterface);
        ByteArrayEntity entity = new ByteArrayEntity(b);
        client.post(context, url, header, entity, null, responseHandler);
    }

    /**
     * 设置Cookie，之后从Server获取到的cookies将被保存至PersistentCookieStore
     *
     * @param context
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
        private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
        private static final int DEFAULT_MAX_RETRIES = 2;
        private static final int DEFAULT_RETRY_SLEEP_TIME_MILLIS = 200;

        /**
         * 初始化HttpClient配置信息
         *
         * @param asyncHttpClient
         */
        public static void initConfig(AsyncHttpClient asyncHttpClient) {
            // 设置线程
            // asyncHttpClient.setThreadPool((ThreadPoolExecutor) Executors
            // .newFixedThreadPool(DEFAULT_MAX_CONNECTIONS));
            asyncHttpClient.setMaxConnections(DEFAULT_MAX_CONNECTIONS);
            asyncHttpClient.setTimeout(DEFAULT_SOCKET_TIMEOUT);
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
}
