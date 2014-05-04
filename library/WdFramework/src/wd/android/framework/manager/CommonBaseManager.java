package wd.android.framework.manager;

import wd.android.framework.global.GlobalData;
import wd.android.util.global.MyPreference;
import android.content.Context;

public class CommonBaseManager extends BaseManager {

	@Override
	protected void onCreate(Context context) {
		addService(new GlobalData());
		addService(new MyPreference(context, context.getPackageName()));
	}

	@Override
	protected void onDestroy() {
		ServiceHolder.getService(GlobalData.class).release();
		ServiceHolder.getService(MyPreference.class).commitTransaction();
	}
}
