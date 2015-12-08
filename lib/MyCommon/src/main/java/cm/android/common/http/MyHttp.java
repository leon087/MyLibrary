package cm.android.common.http;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;

import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.Header;

public class MyHttp {

    private final WeakReference<Context> context;

    private String tag;

    // private Context context;

    public MyHttp(Context context, String tag) {
        // this.context = context;
        this.context = new WeakReference<Context>(context.getApplicationContext());
        if (tag == null) {
            this.tag = context.getPackageName();
        } else {
            this.tag = tag;
        }
    }

    public void cancel() {
        Http.getAsync().cancel(tag);
    }

    public <T> void exec(String url, RequestParams params, HttpListener<T> httpHandler) {
        exec(url, null, params, httpHandler);
    }

    public <T> void exec(String url, Header[] headers, RequestParams params,
            HttpListener<T> httpListener) {
        Http.getAsync().exec(context.get(), tag, url, headers, params, httpListener);
    }

    public void exec(String url, Header[] headers, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {
        Http.getAsync().exec(context.get(), tag, url, headers, params, responseHandler);
    }

    public <T> void exec(String url, byte[] data, HttpListener<T> httpListener) {
        exec(url, null, data, httpListener);
    }

    public <T> void exec(String url, Header[] header, byte[] data, HttpListener<T> httpListener) {
        Http.getAsync().exec(context.get(), tag, url, header, data, httpListener);
    }

    public void exec(String url, Header[] header, byte[] data,
            AsyncHttpResponseHandler responseHandler) {
        Http.getAsync().exec(context.get(), tag, url, header, data, responseHandler);
    }
}
