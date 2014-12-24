package cm.android.common.http.volley;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public interface HttpListener<T> extends Listener<T>, ErrorListener {
    // void onStart();

    // void onFinish();

    T parseData(byte[] data, String charset);
}
