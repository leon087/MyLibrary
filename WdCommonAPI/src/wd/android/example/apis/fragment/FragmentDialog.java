package wd.android.example.apis.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import wd.android.app.ui.activity.MyBaseActivity;
import wd.android.app.ui.fragment.dialog.ConfirmDialog;
import wd.android.app.ui.fragment.dialog.LoadingDialog;
import wd.android.wdcommondapi.R;

/**
 * Created by simon on 14-3-31.
 */
public class FragmentDialog extends MyBaseActivity {

    private LoadingDialog mLoadingDialog;
    private ConfirmDialog confirmDialog;

    public View.OnClickListener myOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mybasedialog_btn:
                    showLoadingDialog();
                    break;
                case R.id.defaultdialog_btn:
                    showConfirmDialog();
                    break;
                default:
                    break;
            }

        }
    };

    private void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        mFragmentHelper.showDialog(null, mLoadingDialog);
    }

    private void showConfirmDialog() {
        if (confirmDialog == null) {
            String title = "tips";
            confirmDialog = ConfirmDialog.newInstance(title,
                    "default dialog", false);
            confirmDialog.setCancelable(true);
        }
        mFragmentHelper.showDialog(null, confirmDialog);
    }

    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        Button btn1 = (Button) rootView.findViewById(R.id.mybasedialog_btn);
        Button btn2 = (Button) rootView.findViewById(R.id.defaultdialog_btn);
        btn1.setOnClickListener(myOnClickListener);
        btn2.setOnClickListener(myOnClickListener);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_dialog;
    }

    @Override
    public void onBeforeContentView(Bundle savedInstanceState) {
        //super.onBeforeContentView(savedInstanceState);
    }

}
