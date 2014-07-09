package cm.android.app.ui.fragment.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import cm.android.app.global.Tag;
import cm.android.app.ui.MyUIUtil;
import cm.android.app.ui.activity.MyBaseActivity;
import cm.android.custom.http.BaseHttpListener;
import cm.android.framework.ui.BaseDialogFragment;
import cm.android.util.util.MapUtil;
import cm.android.util.util.Utils;
import cm.android.wdcommondapi.R;

import java.util.Map;

public abstract class MyBaseDialog extends BaseDialogFragment implements
		IBaseDialogInterface {

	protected static int MYTHEME1 = R.style.Theme_Base_Dialog_Fragment_1;
	protected static int MYTHEME2 = R.style.Theme_Base_Dialog_Fragment_2;
	protected static int MYTHEME3 = R.style.Theme_Base_Dialog_Fragment_3;
	protected static int MYTHEME4 = R.style.Theme_Base_Dialog_Fragment_4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
	}

	@Override
	public void initView(View rootView, Bundle bundle) {
		setupView(rootView, bundle);
	}

	@Override
	public void initData(Bundle bundle) {
		setupData(bundle);
		setDialogLocation();
	}

	@Override
	public int getRootViewId() {
		return getConvertViewId();
	}

	// public void setDialogDimension(int width, int height, int gravity) {
	// this.getDialog().setCancelable(false);
	// this.getDialog().setCanceledOnTouchOutside(false);
	// Window window = this.getDialog().getWindow();
	//
	// // DisplayMetrics dm = new DisplayMetrics();
	// // window.getWindowManager().getDefaultDisplay().getMetrics(dm);
	// // MyLog.d("(WxH:density)===========" + dm.widthPixels + "x"
	// // + dm.heightPixels + " : " + dm.density);
	//
	// WindowManager.LayoutParams lp = window.getAttributes();
	// MyLog.d("========" + lp.width + "===========" + lp.height);
	// // 这里设置也不起作用
	// // lp.width = width;
	// // lp.height = height;
	// lp.gravity = gravity;
	// window.setAttributes(lp);
	// }

	public void setDialogLocation() {
		this.getDialog().setCancelable(cancelable());
		this.getDialog().setCanceledOnTouchOutside(cancelable());
		Window window = this.getDialog().getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = displayWindowLocation();
		window.setAttributes(lp);
	}

	public abstract int getTheme();

	public abstract int displayWindowLocation();

	public abstract boolean cancelable();

	public class MyBaseHttpHandler extends
			BaseHttpListener<Map<String, Object>> {

		@Override
		protected void onSuccess(Map<String, String> headers,
				Map<String, Object> responseMap) {
		}

		@Override
		protected void onFailure(Throwable error,
				Map<String, Object> responseMap) {
			super.onFailure(error, responseMap);
			String resultMsg = MapUtil.getString(responseMap, Tag.MSG);
			if (Utils.isEmpty(resultMsg)) {
				resultMsg = getString(R.string.http_exception);
			}
			MyUIUtil.showToast(resultMsg);
		}

		@Override
		public void onStart() {
			((MyBaseActivity) mActivity).showLoadingHandler
					.sendEmptyMessage(MyBaseActivity.MSG_SHOW_PROGRESS);
		}

		@Override
		public void onFinish() {
			((MyBaseActivity) mActivity).showLoadingHandler
					.sendEmptyMessage(MyBaseActivity.MSG_DISMISS_PROGRESS);
		}

	};

}
