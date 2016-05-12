package cm.android.app.mvp.login;

import cm.android.app.mvp.BasePresenter;
import cm.android.app.mvp.BaseView;

public class LoginContract {
    interface Presenter extends BasePresenter {
        void login(String name, String pwd);
    }

    interface View extends BaseView<Presenter> {
        void showErrorTip(String msg);

        void showSuccessTip();

        void showProgress();

        void dismissProgress();
    }
}
