package cm.android.app.ui.fragment.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import cm.android.wdcommondapi.R;

public class LoadingDialog extends MyBaseDialog {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setupView(View rootView, Bundle bundle) {
	}

	@Override
	public void setupData(Bundle bundle) {

	}

	@Override
	public int getConvertViewId() {
		return R.layout.dialog_fragment_loading;
	}

	@Override
	public int getTheme() {
		return MYTHEME3;
	}

	@Override
	public int displayWindowLocation() {
		return Gravity.CENTER;
	}

	@Override
	public boolean cancelable() {
		return true;
	}

}
