package cm.android.example.apis.common.cachedemo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cm.android.app.ui.activity.MyBaseActivity;
import cm.android.common.http.CacheHttpListener;
import cm.android.common.http.HttpLoader;
import cm.android.util.util.MyLog;
import cm.android.util.util.UIUtils;
import cm.android.wdcommondapi.R;
import org.apache.http.Header;

import java.util.Map;

//import android.util.Base64;

public class CacheDemoActivity extends MyBaseActivity {

    private Button btnGetData = null;
    private Button btnClearCache = null;
    private TextView txtCacheMsg = null;
    private String httpUrl = "http://www.lvmama.com/search/autoCompletePlace.do?callback=jQuery17209974897874481706_1396938109126&keyword=%E6%98%86%E5%B1%B1&fromDestId=79&fromChannel=&newChannel=&_=1396938163085";
    // private String httpUrl =
    // "http://dict.hjenglish.com/json/daily/en?cb=onDone";
    private StringBuilder buffer = null;

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        MyLog.e("initView");
        btnGetData = UIUtils.findView(rootView, R.id.btn_get_data);
        btnGetData.setOnClickListener(clickListener);
        btnClearCache = UIUtils.findView(rootView, R.id.btn_clear_cache);
        btnClearCache.setOnClickListener(clickListener);
        txtCacheMsg = UIUtils.findView(rootView, R.id.cache_text_msg);
    }

    public OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int vId = v.getId();
            switch (vId) {
                case R.id.btn_get_data:
                    txtCacheMsg.setText("");
                    buffer.delete(0, buffer.length());
                    HttpLoader.load(httpUrl, httpHandler);
                    break;
                case R.id.btn_clear_cache:
                    break;
            }
        }
    };
    public CacheHttpListener<Map<String, Object>> httpHandler = new CacheHttpListener<Map<String, Object>>() {

        @Override
        protected void onSuccess(int statusCode, Map<String, String> headers,
                                 Map<String, Object> responseMap) {

            buffer.append("onSuccess()...");
            buffer.append("\n");
            buffer.append(responseMap.toString());
            txtCacheMsg.setText(buffer.toString());
        }

        @Override
        protected void onFinish() {
            super.onFinish();
            buffer.append("onFinish()...");
            buffer.append("\n");
            txtCacheMsg.setText(buffer.toString());
        }

        @Override
        protected void onStart() {
            super.onStart();
            buffer.append("onStart()...");
            buffer.append("\n");
            txtCacheMsg.setText(buffer.toString());
        }

        @Override
        protected void onFailure(Throwable error,
                                 Map<String, Object> responseMap) {
            super.onFailure(error, responseMap);
            buffer.append("onFailure()...");
            buffer.append("\n");
            buffer.append("" + android.util.Log.getStackTraceString(error));
            buffer.append("\n");
            txtCacheMsg.setText(buffer.toString());
        }

        @Override
        protected Map<String, Object> parseResponse(Header[] headers,
                                                    byte[] responseBytes) throws Throwable {
            String response = new String(responseBytes);

            // 测试的httpUrl返回的数据包含多余的数据，筛选去除，保留json部分
            // 如果测试数据为纯json，则不需要重写parseResponse
            if (response.contains("(")) {
                response = response.substring(response.indexOf("(") + 1,
                        response.indexOf(")"));
            }
            return super.parseResponse(headers, response.getBytes());
        }

    };

    @Override
    public void initData(Bundle savedInstanceState) {
        MyLog.e("initData");
        // 初始化缓存大小，目录，内容
        HttpLoader.init(this);
        if (null != savedInstanceState) {
            buffer = new StringBuilder(savedInstanceState.getString("buffer"));
        } else {
            buffer = new StringBuilder();
        }
        txtCacheMsg.setText(buffer.toString());

    }

    @Override
    public int getRootViewId() {
        return R.layout.apis_cachedemo_activity;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("buffer", buffer.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        // buffer = new StringBuilder(bundle.getString("buffer"));
        // txtCacheMsg.setText(buffer.toString());

    }

}
