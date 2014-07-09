package cm.android.example.apis.common.imagedemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cm.android.app.ui.activity.MyBaseActivity;
import cm.android.app.ui.fragment.dialog.LoadingDialog;
import cm.android.common.image.ImageManager;
import cm.android.util.util.EnvironmentInfo;
import cm.android.wdcommondapi.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

/**
 * Created by jeme on 14-4-3.
 */
public class ImageDemoActivity extends MyBaseActivity {

	private LoadingDialog mLoadingDialog;
	private TextView txtTipUrl = null;
	private Button btnImageLoad = null;
	private Button btnClearCache = null;
	private Button btnClearUrlCache = null;
	private ImageView ivLoad = null;
	private ImageManager imageManager = null;
	private Bitmap loadedImage = null;
	private String imageUrl = "http://imgwww.heiguang.net/f/2013/0614/20130614090801888.jpg";

	@Override
	public void initView(View rootView, Bundle savedInstanceState) {
		ivLoad = (ImageView) rootView.findViewById(R.id.iv_load);
		txtTipUrl = (TextView) rootView.findViewById(R.id.txtTipUrl);
		btnImageLoad = (Button) rootView.findViewById(R.id.btn_load_image);
		btnImageLoad.setOnClickListener(clickListener);
		btnClearCache = (Button) rootView
				.findViewById(R.id.btn_clear_flag_cache);
		btnClearCache.setOnClickListener(clickListener);
		btnClearUrlCache = (Button) rootView
				.findViewById(R.id.btn_clear_url_cache);
		btnClearUrlCache.setOnClickListener(clickListener);
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		txtTipUrl.setText("默认加载的图片地址是：\r\n" + imageUrl);

		imageManager = new ImageManager();
		File cacheDir = EnvironmentInfo.getDiskCacheDir(this, "http");
		// 默认的图片加载 缓存保存在内存和sd卡中
		imageManager.init(this, cacheDir.getAbsolutePath(), R.drawable.logo);
		
		if(null != savedInstanceState){
			loadedImage = savedInstanceState.getParcelable("loadedImage");
			ivLoad.setImageBitmap(loadedImage);
		}

	}

	@Override
	public int getRootViewId() {
		return R.layout.apis_imagedemo_activity;
	}

	public OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int vId = v.getId();
			switch (vId) {
			case R.id.btn_load_image:
				if (imageManager != null) {
					// 不设置图片加载侦听器
					// imageManager.loadImage(imageUrl,ivLoad);

					imageManager.loadImage(imageUrl, ivLoad, imageLoadListener);
				}
				break;
			case R.id.btn_clear_flag_cache:
				if (imageManager != null) {
					// ImageManager.clearCache(int flag)
					// flag可以为：
					// ImageManager.CLEAR_CACHE_DISC 清空sd卡中的缓存
					// ImageManager.CLEAR_CACHE_MEMORY 清空内存中的缓存
					// ImageManager.CLEAR_CACHE_ALL 清空内存和sd卡中的缓存
					imageManager.clearCache(ImageManager.CLEAR_CACHE_DISC);
					txtTipUrl.setText("清空sd卡中缓存");
				}
				break;
			case R.id.btn_clear_url_cache:
				if (imageManager != null) {
					imageManager.clearCache(imageUrl);
					txtTipUrl.setText("清空" + imageUrl + "所有缓存");
				}
				break;
			}
		}
	};
	public ImageLoadingListener imageLoadListener = new ImageLoadingListener() {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			txtTipUrl.setText("开始加载图片，地址是：\r\n" + imageUrl);
			showLoadingDialog();
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			txtTipUrl.setText("图片加载失败，地址是：\r\n" + imageUrl);
			hideLoadingDialog();
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			txtTipUrl.setText("网络图片加载完成，地址是：\r\n" + imageUrl);
			ImageDemoActivity.this.loadedImage = loadedImage;
			hideLoadingDialog();
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			txtTipUrl.setText("取消加载图片，地址是：\r\n" + imageUrl);
			hideLoadingDialog();
		}
	};

	private void showLoadingDialog() {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog();
		}
		mFragmentHelper.showDialog(null, mLoadingDialog);

	}
	
	private void hideLoadingDialog() {
		if (mLoadingDialog != null) {
			mFragmentHelper.removeDialogFragment(mLoadingDialog);
			mLoadingDialog = null;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("loadedImage", loadedImage);
	}
	
	
}
