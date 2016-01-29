package cm.android.common.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class Http {

    private static final int DEFAULT_MAX_REQUEST = 20;

    private static final int DEFAULT_TIMEOUT = 15 * 1000;

    private static final MediaType MEDIA_TYPE = MediaType.parse(
            "application/x-www-form-urlencoded; charset=UTF-8");

    private static final MediaType MEDIA_TYPE_JSON = MediaType
            .parse("application/json; charset=UTF-8");

    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=UTF-8");


    private static OkHttpClient config() {
//        client.setCache(new Cache(context.getCacheDir(), maxCacheSize));
//        client.newBuilder().connectTimeout().build();
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//        builder.cache(new Cache(context.getCacheDir(), 50 * 1024 * 1024L));

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(DEFAULT_MAX_REQUEST);
        builder.dispatcher(dispatcher);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
//        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
//        builder.retryOnConnectionFailure(true);

        //gzip
        builder.addInterceptor(new GzipInterceptor());
        builder.addInterceptor(new RetryInterceptor(2));

        //log
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(loggingInterceptor);

        return builder.build();
    }

    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (Http.class) {
                if (client == null) {
                    client = config();
                }
            }
        }
        return client;
    }

    public void cancel() {
        getClient().dispatcher().cancelAll();
    }

    private static Request createRequest(String url, Headers headers, byte[] body) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (body != null) {
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_TEXT, body);
            builder.post(requestBody);
        }

        if (headers != null) {
            builder.headers(headers);
        }
//        builder.header("Content-Encoding", "gzip");
//        builder.addHeader()
        Request request = builder.build();
        return request;
    }

    public static Call exec(String url, Headers headers, byte[] body, Callback responseCallback) {
        Call call = newCall(url, headers, body);
        call.enqueue(responseCallback);
        return call;
    }

    public static Response execSync(String url, Headers headers, byte[] body) throws IOException {
        Response response = newCall(url, headers, body).execute();
        if (!response.isSuccessful()) {
            throw new IOException("code = " + response.code());
        }

        return response;
    }

    public static Call newCall(String url, Headers headers, byte[] body) {
        Request request = createRequest(url, headers, body);
        Call call = getClient().newCall(request);
        return call;
    }

    public static boolean isGzipSupport(Headers headers) {
        String headEncoding = headers.get("Accept-Encoding");
        if (headEncoding == null || (!headEncoding.contains("gzip"))) { // 客户端 不支持 gzip
            return false;
        } else { // 支持 gzip 压缩
            return true;
        }
    }
}
