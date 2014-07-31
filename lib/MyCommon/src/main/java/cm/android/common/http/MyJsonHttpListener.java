package cm.android.common.http;

import cm.android.util.ReflectionUtil;
import cm.android.util.Utils;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;

public abstract class MyJsonHttpListener<T> extends HttpListener<T> {
    protected Class<T> clazz = null;

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public MyJsonHttpListener() {
        this.clazz = (Class<T>) ReflectionUtil
                .getSuperClassGenricType(getClass());
    }

    @Override
    protected T parseResponse(Header[] headers, byte[] responseBytes)
            throws Throwable {
        String rawJsonData = Utils.getString(responseBytes,
                AsyncHttpResponseHandler.DEFAULT_CHARSET);
        return JSON.parseObject(rawJsonData, clazz);
    }
}
