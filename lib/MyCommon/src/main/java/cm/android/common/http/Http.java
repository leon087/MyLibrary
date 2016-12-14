package cm.android.common.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cm.android.net.HttpsUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public final class Http {

    private static final int DEFAULT_MAX_REQUEST = 20;

    private static final int DEFAULT_TIMEOUT = 15 * 1000;

    private static final MediaType MEDIA_TYPE = MediaType.parse(
            "application/x-www-form-urlencoded; charset=UTF-8");

    private static final MediaType MEDIA_TYPE_JSON = MediaType
            .parse("application/json; charset=UTF-8");

    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=UTF-8");

    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_ENCODING_GZIP = "gzip";

    public static final Logger logger = LoggerFactory.getLogger("http");

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
//        builder.addInterceptor(new GzipInterceptor());
        builder.addInterceptor(new RetryInterceptor(2));

        return builder.build();
    }

    private static volatile OkHttpClient client;

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

    public static HttpLoggingInterceptor createLogging(boolean debug) {
        //log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                logger.info(message);
            }
        });

        if (debug) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        }
        return loggingInterceptor;
    }

    public void cancel() {
        getClient().dispatcher().cancelAll();
    }

    public static void cancelTag(OkHttpClient client, Object tag) {
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
                return;
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
                return;
            }
        }
    }

    private static Request createRequest(final Object tag, String url, Headers headers, byte[] body) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (tag != null) {
            builder.tag(tag);
        }

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

    public static Call enqueue(final Object tag, String url, Headers headers, byte[] body, Callback responseCallback) {
        Call call = newCall(tag, url, headers, body);
        call.enqueue(responseCallback);
        return call;
    }

    public static Response execute(final Object tag, String url, Headers headers, byte[] body) throws IOException {
        Response response = newCall(tag, url, headers, body).execute();
        if (response == null) {
            throw new IOException("response = null");
        }

        if (!response.isSuccessful()) {
            throw new IOException("code = " + response.code());
        }

        return response;
    }

    public static Call newCall(final Object tag, String url, Headers headers, byte[] body) {
        Request request = createRequest(tag, url, headers, body);
        Call call = getClient().newCall(request);
        return call;
    }

    public static boolean isGzipSupport(Headers headers) {
        String headEncoding = headers.get("Accept-Encoding");
        if (headEncoding == null || (!headEncoding.contains(CONTENT_ENCODING_GZIP))) { // 客户端 不支持 gzip
            return false;
        } else { // 支持 gzip 压缩
            return true;
        }
    }

    /**
     * Returns whether encode type indicates the body needs to be gzip-ed.
     */
    public static boolean isGzipEncoding(@Nullable final String encodingType) {
        return CONTENT_ENCODING_GZIP.equalsIgnoreCase(encodingType);
    }

    /**
     * Config ssl.自签名证书,CA证书无需配置
     *
     * @param builder the builder
     * @param tmf     the tmf
     * @param kmf     the kmf
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyManagementException   the key management exception
     */
    public static void sslSocketFactory(OkHttpClient.Builder builder, TrustManagerFactory tmf, KeyManagerFactory kmf)
            throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = HttpsUtil.getSSLContext(tmf, kmf);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) tmf.getTrustManagers()[0]);

//        builder.sslSocketFactory(sslSocketFactory, new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] chain, String authType)
//                    throws CertificateException {
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] chain, String authType)
//                    throws CertificateException {
//                for (X509Certificate cert : chain) {
//                    // Make sure that it hasn't expired.
//                    cert.checkValidity();
//
//                    // Verify the certificate's public key chain.
//                    try {
//                        cert.verify(cert.getPublicKey());
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (InvalidKeyException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchProviderException e) {
//                        e.printStackTrace();
//                    } catch (SignatureException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[0];
//            }
//        });
    }
}
