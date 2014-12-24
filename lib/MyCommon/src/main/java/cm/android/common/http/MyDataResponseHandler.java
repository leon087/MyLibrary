package cm.android.common.http;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cm.android.util.Utils;

public class MyDataResponseHandler<T> extends HttpResponseHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger("HTTP");

    private HttpListener<T> httpListener;

    public MyDataResponseHandler(HttpListener<T> httpListener) {
        this.httpListener = httpListener;
    }

    @Override
    public void onFinish() {
        super.onFinish();
        httpListener.onFinish();
        if (logger.isDebugEnabled()) {
            logger.debug("url = " + getRequestURI());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        httpListener.onStart();
        if (logger.isDebugEnabled()) {
            logger.debug("url = " + getRequestURI());
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
