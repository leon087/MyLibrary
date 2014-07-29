package cm.android.example.apis.view;

import android.os.Bundle;
import android.view.View;
import cm.android.app.ui.activity.MyBaseActivity;
import cm.android.common.ui.view.MyTextView;
import cm.android.util.util.UIUtils;
import cm.android.wdcommondapi.R;

public class MyTextViewActivity extends MyBaseActivity {

    private MyTextView myTextView = null;

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        myTextView = UIUtils.findView(rootView, R.id.my_text_view);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        myTextView.setText("MyTextView Test");
    }

    @Override
    public int getRootViewId() {
        return R.layout.my_textview;
    }

}
