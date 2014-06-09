package wd.android.common.http;

import org.apache.http.Header;

import wd.android.util.util.GenericsUtil;
import wd.android.util.util.Utils;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class MyJsonHttpListener<T> extends HttpListener<T> {
	protected Class<T> clazz = null;

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public MyJsonHttpListener() {
		this.clazz = (Class<T>) GenericsUtil
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
