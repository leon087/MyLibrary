//package cm.android.common.http;
//
//import com.alibaba.fastjson.JSON;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//
//import cm.java.util.ReflectUtil;
//import cm.java.util.Utils;
//import cz.msebera.android.httpclient.Header;
//
//public abstract class MyJsonHttpListener<T> extends HttpListener<T> {
//
//    protected Class<T> clazz = null;
//
//    /**
//     *
//     */
//    @SuppressWarnings("unchecked")
//    public MyJsonHttpListener() {
//        this.clazz = (Class<T>) ReflectUtil
//                .getSuperClassGenricType(getClass());
//    }
//
//    @Override
//    protected T parseResponse(Header[] headers, byte[] responseBytes)
//            throws Throwable {
//        String rawJsonData = Utils.getString(responseBytes,
//                AsyncHttpResponseHandler.DEFAULT_CHARSET);
//        return JSON.parseObject(rawJsonData, clazz);
//    }
//}
