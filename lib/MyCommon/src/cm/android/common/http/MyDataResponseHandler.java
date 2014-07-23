package cm.android.common.http;

import cm.android.util.Utils;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyDataResponseHandler<T> extends HttpResponseHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger("HTTP");
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
        if (logger.isDebugEnabled()) {
            logger.debug("url = " + url);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        httpListener.onStart();
        if (logger.isDebugEnabled()) {
            logger.debug("url = " + url);
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers,
                          byte[] responseBytes, T response) {
        logger.info("statusCode = " + statusCode);
        if (logger.isDebugEnabled()) {
            logger.debug("header:----------------------------------------");
            for (Header header : headers) {
                logger.debug(header.getName() + ":" + header.getValue());
            }
            logger.debug("responseBody:----------------------------------------");
            String responseBody = Utils.getString(responseBytes, getCharset());
            logger.debug(responseBody);
        }

        httpListener.onSuccess(statusCode, headers, responseBytes, response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable, byte[] responseBytes, T errorResponse) {
        httpListener.onFailure(throwable, errorResponse);
        String responseBody = Utils.getString(responseBytes, getCharset());
        logger.error(responseBody, throwable);
    }

    @Override
    protected T parseResponse(Header[] headers, byte[] responseBytes,
                              boolean isFailure) throws Throwable {
        return httpListener.parseResponse(headers, responseBytes);
    }
}
