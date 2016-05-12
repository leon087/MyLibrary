package cm.android.app.mvp.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cm.android.app.sample.R;

public class LoginActivity extends AppCompatActivity {
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mvp_login_activity);

        loginPresenter = new LoginPresenter(new LoginView(this), new ModelRepository());
        loginPresenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.deInit();
    }
}
