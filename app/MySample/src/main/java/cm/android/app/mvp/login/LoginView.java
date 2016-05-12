package cm.android.app.mvp.login;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import cm.android.app.sample.R;

public class LoginView implements LoginContract.View {
    private LoginContract.Presenter presenter;

    private EditText userName;
    private EditText userPwd;
    private Button btnLogin;

    private LoginActivity activity;
    private ProgressDialog progressDialog;

    private void login() {
        String name = userName.getText().toString();
        String pwd = userPwd.getText().toString();

        presenter.login(name, pwd);
    }

    public LoginView(LoginActivity activity) {
        this.activity = activity;
    }

    @Override
    public void showErrorTip(String msg) {
        dismissProgress();
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessTip() {
        dismissProgress();
        Toast.makeText(activity, "loginSuccess", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.show();
    }

    @Override
    public void init(@NonNull LoginContract.Presenter presenter) {
        this.presenter = presenter;

        userName = ButterKnife.findById(this.activity, R.id.user_name);
        userPwd = ButterKnife.findById(this.activity, R.id.user_pwd);
        btnLogin = ButterKnife.findById(this.activity, R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void dismissProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
