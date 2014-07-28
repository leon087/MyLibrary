package cm.android.app.ui.fragment.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import cm.android.app.global.Tag;

public class UpgradeDialog extends DialogFragment {

	private UpgradeDialog() {
	}

	public static UpgradeDialog newInstance(String title, String content) {
		UpgradeDialog frag = new UpgradeDialog();
		Bundle bundle = new Bundle();
		bundle.putString(Tag.TITLE, title);
		bundle.putString(Tag.CONTENT, content);
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		String title = "";
		String content = "";
		if (bundle != null) {
			title = bundle.getString(Tag.TITLE);
			content = bundle.getString(Tag.CONTENT);
		}

		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setTitle(title);
		dialog.setMessage(content);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		return dialog;
	}
}
