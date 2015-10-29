package cm.android.common.http;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;

public abstract class HttpResponseHandler<DATA_TYPE> extends
        AsyncHttpResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger("HTTP");

    public abstract void onSuccess(int statusCode, Header[] headers,
            byte[] responseBytes, DATA_TYPE response);

    public abstract void onFailure(int statusCode, Header[] headers,
            Throwable throwable, byte[] responseBytes, DATA_TYPE errorResponse);

    @Override
    public final void onSuccess(final int statusCode, final Header[] headers,
            final byte[] responseBytes) {
        if (statusCode != HttpStatus.SC_NO_CONTENT) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final DATA_TYPE dataResponse = parseResponse(headers,
                                responseBytes, false);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onSuccess(statusCode, headers, responseBytes,
                                        dataResponse);
                            }
                        });
                    } catch (final Throwable t) {
                        logger.debug("parseResponse thrown an problem", t);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, t,
                                        responseBytes, null);
                            }
                        });
                    }
                }
            }).start();
        } else {
            onSuccess(statusCode, headers, null, null);
        }
    }

    @Override
    public final void onFailure(final int statusCode, final Header[] headers,
            final byte[] responseBytes, final Throwable throwable) {
        if (responseBytes != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final DATA_TYPE dataResponse = parseResponse(headers,
                                responseBytes, true);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, throwable,
                                        responseBytes, dataResponse);
                            }
                        });
                    } catch (Throwable t) {
                        logger.debug("parseResponse thrown an problem", t);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                onFailure(statusCode, headers, throwable,
                                        responseBytes, null);
                            }
                        });
                    }
                }
            }).start();
        } else {
            onFailure(statusCode, headers, throwable, null, null);
        }
    }

    protected abstract DATA_TYPE parseResponse(Header[] headers,
            byte[] responseBytes, boolean isFailure) throws Throwable;

}
