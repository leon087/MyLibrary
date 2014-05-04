package wd.android.common.http;

import java.util.Map;

import org.apache.http.Header;

import wd.android.util.util.Utils;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class MyJsonHttpListener extends HttpListener {
	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> parseResponse(Header[] headers,
			byte[] responseBytes) throws Throwable {
		String rawJsonData = Utils.getString(responseBytes,
				AsyncHttpResponseHandler.DEFAULT_CHARSET);
		return JSON.parseObject(rawJsonData, Map.class);
	}
}
