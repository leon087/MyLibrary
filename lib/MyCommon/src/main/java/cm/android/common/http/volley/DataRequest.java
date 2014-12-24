package cm.android.common.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public final class DataRequest<T> extends Request<T> {

    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

    public static final String ENCODING_GZIP = "gzip";

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 2500;

    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    private HttpListener<T> mListener;

    private byte[] mRequestBody;

    private final Map<String, String> mRequestBodyMap = new HashMap<String, String>();

    private final Map<String, String> mHeaderMap = new HashMap<String, String>();

    public DataRequest(String url, byte[] requestBody, HttpListener<T> listener) {
        super(requestBody == null ? Method.GET : Method.POST, url, listener);
        mRequestBody = requestBody;
        init(url, listener);
    }

    public DataRequest(String url, Map<String, String> bodyMap, HttpListener<T> listener) {
        super(bodyMap == null || bodyMap.isEmpty() ? Method.GET : Method.POST,
                url, listener);
        if (bodyMap != null && !bodyMap.isEmpty()) {
            mRequestBodyMap.putAll(bodyMap);
        }
        init(url, listener);
    }

    private void init(String url, HttpListener<T> listener) {
        mListener = listener;
        mHeaderMap.put(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES,
                DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    private byte[] parseResponse(NetworkResponse response)
            throws UnsupportedEncodingException {
        Map<String, String> headers = response.headers;
        String acceptEncoding = headers.get(HEADER_ACCEPT_ENCODING);
        if (!ENCODING_GZIP.equals(acceptEncoding)) {
            return response.data;
        } else {
            return VolleyUtil.decompressGZip(response.data);
        }
    }

    @Override
    protected final Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            byte[] data = parseResponse(response);
            return Response.success(
                    parseData(data, HttpHeaderParser.parseCharset(response.headers)),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }

    private T parseData(byte[] data, String charset) {
        return mListener.parseData(data, charset);
    }

    @Override
    public final byte[] getBody() {
        if (mRequestBody != null) {
            return mRequestBody;
        }

        if (mRequestBodyMap == null || mRequestBodyMap.isEmpty()) {
            return null;
        }

        try {
            return super.getBody();
        } catch (AuthFailureError e) {
            VolleyLog.e("AuthFailureError... e = %s,bodyMap = %s.", e,
                    mRequestBodyMap.toString());
            return null;
        }
    }

    @Override
    protected final Map<String, String> getParams() throws AuthFailureError {
        return mRequestBodyMap;
    }

    @Override
    public final Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaderMap;
    }

    public void addHeader(Map<String, String> headerMap) {
        if (null != headerMap && !headerMap.isEmpty()) {
            mHeaderMap.putAll(headerMap);
        }
    }

    public void addHeader(String name, String value) {
        mHeaderMap.put(name, value);
    }
}
