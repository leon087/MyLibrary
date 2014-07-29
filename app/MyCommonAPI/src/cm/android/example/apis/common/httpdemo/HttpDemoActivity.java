package cm.android.example.apis.common.httpdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cm.android.app.ui.activity.MyBaseActivity;
import cm.android.common.http.HttpListener;
import cm.android.common.http.MyHttp;
import cm.android.custom.http.BaseHttpListener;
import cm.android.util.util.UIUtils;
import cm.android.wdcommondapi.R;

import java.util.Map;

public class HttpDemoActivity extends MyBaseActivity {
    private TextView textLog;
    private EditText editUrl;
    private Button btnPost;
    private StringBuilder buffer = new StringBuilder();
    private MyHttp myHttp;

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        editUrl = UIUtils.findView(this, R.id.httpdemo_edit_url);
        btnPost = UIUtils.findView(this, R.id.httpdemo_btn);
        textLog = UIUtils.findView(this, R.id.httpdemo_text_msg);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buffer.delete(0, buffer.length());
                String url = editUrl.getText().toString();
                myHttp.exec(url, httpListener);
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        myHttp = new MyHttp(this);
    }

    @Override
    public void onDestroyActivity() {
        super.onDestroyActivity();
        myHttp.cancel();
    }

    @Override
    public int getRootViewId() {
        return R.layout.apis_httpdemo_activity;
    }

    private HttpListener<Map<String, Object>> httpListener = new BaseHttpListener<Map<String, Object>>() {

        @Override
        protected void onSuccess(Map<String, String> headers,
                                 Map<String, Object> responseMap) {
            buffer.append("onSuccess()...");
            buffer.append("\n");
            textLog.setText(buffer.toString());
        }

        @Override
        protected void onStart() {
            super.onStart();
            buffer.append("onStart()...");
            buffer.append("\n");
            textLog.setText(buffer.toString());
        }

        ;

        @Override
        protected void onFinish() {
            super.onFinish();
            buffer.append("onFinish()...");
            buffer.append("\n");
            textLog.setText(buffer.toString());
        }

        ;

        @Override
        protected void onFailure(Throwable error,
                                 java.util.Map<String, Object> responseMap) {
            super.onFailure(error, responseMap);
            buffer.append("onFailure()...");
            buffer.append("\n");
            buffer.append("" + android.util.Log.getStackTraceString(error));
            buffer.append("\n");
            textLog.setText(buffer.toString());
        }

        ;
    };

}
