package wd.android.example.apis.view;

import wd.android.app.ui.activity.MyBaseActivity;
import wd.android.common.ui.view.MyTextView;
import wd.android.util.util.UIUtils;
import wd.android.wdcommondapi.R;
import android.os.Bundle;
import android.view.View;

public class MyTextViewActivity extends MyBaseActivity{

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
