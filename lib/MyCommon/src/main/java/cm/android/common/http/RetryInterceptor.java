package cm.android.common.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptor implements Interceptor {
    private int count;

    public RetryInterceptor(int count) {
        this.count = count;
    }

    private static final Logger logger = LoggerFactory.getLogger("net");

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response response = chain.proceed(request);

        int tryCount = 0;
        while (!response.isSuccessful() && tryCount < count) {
            logger.info("okhttp:proceed:tryCount= {}", tryCount);
            tryCount++;
            response = chain.proceed(request);
        }

        return response;
    }
}
