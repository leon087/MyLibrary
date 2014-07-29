package cm.android.example.apis.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cm.android.app.ui.activity.MyBaseActivity;
import cm.android.common.ui.view.SearchKeywordsView;
import cm.android.util.util.ObjectUtil;
import cm.android.util.util.UIUtils;
import cm.android.wdcommondapi.R;

import java.util.List;

public class SearchKeywordsViewDemoActivity extends MyBaseActivity {

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        SearchKeywordsView keywordsFlow = UIUtils.findView(this,
                R.id.ui_layout_searchkeywords);

        keywordsFlow.setDuration(800l);
        keywordsFlow.setOnItemClickListener(keywordsFlowOnClickListener);
        keywordsFlow.setFocusable(false);
        keywordsFlow.setFocusableInTouchMode(false);
        keywordsFlow.go2Show(SearchKeywordsView.ANIMATION_IN);

        List<String> list = ObjectUtil.newArrayList();
        for (int i = 0; i < 15; i++) {
            list.add("test_" + i);
        }
        feedKeywordsFlow(keywordsFlow, list);
    }

    private OnClickListener keywordsFlowOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            CharSequence text = ((TextView) v).getText();
            UIUtils.showToast(SearchKeywordsViewDemoActivity.this, text);
        }
    };

    private void feedKeywordsFlow(SearchKeywordsView keywordsFlow,
                                  List<String> list) {
        int count = list.size();
        if (count > SearchKeywordsView.MAX) {
            count = SearchKeywordsView.MAX;
        }

        for (int i = 0; i < count; i++) {
            keywordsFlow.feedKeyword(list.get(i));
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getRootViewId() {
        return R.layout.apis_ui_searchkeywordsview_activity;
    }

}
