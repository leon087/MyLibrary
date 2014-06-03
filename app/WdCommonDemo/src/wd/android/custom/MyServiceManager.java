package wd.android.custom;

import java.util.Properties;

import wd.android.app.global.Tag;
import wd.android.app.manager.HttpManager;
import wd.android.common.http.HttpLoader;
import wd.android.common.http.HttpUtil;
import wd.android.common.image.ImageManager;
import wd.android.framework.global.DirData;
import wd.android.framework.manager.CommonBaseManager;
import wd.android.framework.manager.ServiceHolder;
import wd.android.util.sdk.ExternalStorageReceiver;
import wd.android.util.sdk.ExternalStorageReceiver.ExternalStorageListener;
import wd.android.util.util.Utils;
import wd.android.wdcommondemo.R;
import android.content.Context;

public class MyServiceManager extends CommonBaseManager {
	private Context mContext;

	MyServiceManager() {
	}

	private ExternalStorageListener externalStorageListener = new ExternalStorageListener() {

		@Override
		public void onMediaMounted() {
			ServiceHolder.getService(DirData.class).initWorkDir(mContext);
		}

		@Override
		public void onMediaRemoved() {
			ServiceHolder.getService(DirData.class).initWorkDir(mContext);
		}
	};

	@Override
	protected void onCreate(Context context) {
		// addService(new MyDaoManager().init(context));
		super.onCreate(context);

		HttpLoader.init(context);
		mContext = context;
		// // 初始化存储路径，该对象无须释放资源
		DirData dirData = new DirData(context);
		addService(dirData);

		ImageManager imageManager = new ImageManager();
		imageManager.init(context, null, R.drawable.default_bg);
		addService(imageManager);
		addService(new ExternalStorageReceiver(context, externalStorageListener));
		// addService(new ThreadPool(1, 1));

		Properties properties = Utils.loadProperties(context, Tag.CONFIG);
		MyManager.putData(Tag.CONFIG, properties);

		HttpManager.initHeader();
	}

	@Override
	protected void onDestroy() {
		HttpUtil.cancel(mContext);

		// getService(ThreadPool.class).release();
		ServiceHolder.getService(ImageManager.class).deInit();
		ServiceHolder.getService(ExternalStorageReceiver.class).release();
		super.onDestroy();

		// getService(MyDaoManager.class).deInit();
	}

}
