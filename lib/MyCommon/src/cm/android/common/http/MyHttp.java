package cm.android.common.http;

import android.content.Context;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.lang.ref.WeakReference;

public class MyHttp {
    private final WeakReference<Context> context;

    // private Context context;

    public MyHttp(Context context) {
        // this.context = context;
        this.context = new WeakReference<Context>(context);
    }

    /**
     * JSON数据请求
     */
    public void cancel() {
        HttpUtil.cancel(context.get());
    }

    /**
     * JSON数据请求
     *
     * @param <T>
     */
    public <T> void exec(String url, HttpListener<T> httpHandler) {
        exec(url, null, null, httpHandler);
    }

    /**
     * JSON数据请求
     *
     * @param <T>
     */
    public <T> void exec(String url, Header[] headers,
                         HttpListener<T> httpHandler) {
        exec(url, headers, null, httpHandler);
    }

    /**
     * JSON数据请求
     *
     * @param <T>
     */
    public <T> void exec(String url, RequestParams params,
                         HttpListener<T> httpHandler) {
        exec(url, null, params, httpHandler);
    }

    /**
     * JSON数据请求
     *
     * @param <T>
     */
    public <T> void exec(String url, Header[] headers, RequestParams params,
                         HttpListener<T> httpListener) {
        MyDataResponseHandler<T> responseHandler = new MyDataResponseHandler<T>(
                url, httpListener);
        exec(url, headers, params, responseHandler);
    }

    public void exec(String url, AsyncHttpResponseHandler responseHandler) {
        exec(url, null, (RequestParams) null, responseHandler);
    }

    public void exec(String url, RequestParams params,
                     AsyncHttpResponseHandler responseHandler) {
        exec(url, null, params, responseHandler);
    }

    public void exec(String url, Header[] headers, RequestParams params,
                     AsyncHttpResponseHandler responseHandler) {
        HttpUtil.exec(context.get(), url, headers, params, responseHandler);
    }

    public void exec(String url, byte[] b,
                     AsyncHttpResponseHandler responseHandler) {
        HttpUtil.exec(context.get(), url, null, b, responseHandler);
    }

    public void exec(String url, Header[] header, byte[] b,
                     AsyncHttpResponseHandler responseHandler) {
        HttpUtil.exec(context.get(), url, header, b, responseHandler);
    }
}
