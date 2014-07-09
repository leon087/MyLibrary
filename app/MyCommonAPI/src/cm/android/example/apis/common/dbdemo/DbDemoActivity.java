package cm.android.example.apis.common.dbdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cm.android.app.ui.activity.MyBaseActivity;
import cm.android.custom.MyManager;
import cm.android.util.util.UIUtils;
import cm.android.wdcommondapi.R;

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
