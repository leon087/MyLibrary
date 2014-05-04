package wd.android.example.apis.common.dbdemo;

import wd.android.app.ui.activity.MyBaseActivity;
import wd.android.custom.MyManager;
import wd.android.util.util.UIUtils;
import wd.android.wdcommondapi.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DbDemoActivity extends MyBaseActivity {
	private TextView textView;

	@Override
	public void initView(View rootView, Bundle savedInstanceState) {
		textView = UIUtils.findView(this, R.id.dbdemo_testbean);
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		TestDao dao = MyManager.getDao(TestDao.class);

		// insert
		TestBean testBean = new TestBean();
		testBean.setAge(24);
		testBean.setUserName("test");
		dao.insertOrUpdate(testBean);

		// query
		TestBean bean = dao.queryByUserName("test");
		String format = "userName:%s \nage:%s \nid:%s \n";
		textView.setText(String.format(format, bean.getUserName(),
				bean.getAge(), bean.get_id()));
	}

	@Override
	public int getRootViewId() {
		return R.layout.apis_dbdemo_activity;
	}

}
