package cm.android.framework.manager;

import android.content.Context;
import cm.android.framework.global.GlobalData;
import cm.android.global.MyPreference;

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
