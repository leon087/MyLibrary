package cm.android.custom;

import android.content.Context;
import cm.android.app.global.Tag;
import cm.android.app.manager.HttpManager;
import cm.android.cmcommondemo.R;
import cm.android.common.http.HttpLoader;
import cm.android.common.http.HttpUtil;
import cm.android.common.image.ImageManager;
import cm.android.framework.global.DirData;
import cm.android.framework.manager.CommonBaseManager;
import cm.android.framework.manager.ServiceHolder;
import cm.android.util.sdk.ExternalStorageReceiver;
import cm.android.util.sdk.ExternalStorageReceiver.ExternalStorageListener;
import cm.android.util.util.Utils;

import java.util.Properties;

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
