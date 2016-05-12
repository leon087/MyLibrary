package cm.android.app.mvp.login;

import android.os.Message;
import android.support.annotation.NonNull;

import cm.android.sdk.WeakHandler;

public class LoginPresenter implements LoginContract.Presenter {
    @NonNull
    private final LoginContract.View logView;

    @NonNull
    private final ModelRepository modelRepository;

    private static final int MSG_SUCCESS = 0x01;
    private static final int MSG_FAILURE = MSG_SUCCESS + 1;

    private WeakHandler handler = new WeakHandler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUCCESS:
                    logView.showSuccessTip();
                    break;
                case MSG_FAILURE:
                    logView.showErrorTip("login failed");
                    break;
                default:
                    break;
            }
        }
    };

    public LoginPresenter(LoginContract.View view, ModelRepository modelRepository) {
        logView = view;
        this.modelRepository = modelRepository;
    }

    @Override
    public void login(String name, String pwd) {
        logView.showProgress();
        ModelRepository.LoginModel model = new ModelRepository.LoginModel();
        model.name = name;
        model.pwd = pwd;
        modelRepository.login(model, new ModelRepository.LoginCallback() {
            @Override
            public void onSuccess() {
                handler.get().sendEmptyMessage(MSG_SUCCESS);
            }

            @Override
            public void onFailure() {
                handler.get().sendEmptyMessage(MSG_FAILURE);
            }
        });
    }

    @Override
    public void init() {
        logView.init(this);
    }

    @Override
    public void deInit() {
        handler.get().removeCallbacksAndMessages(null);
        logView.dismissProgress();
        modelRepository.cancel();
    }
}
