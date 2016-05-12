package cm.android.app.mvp.login;

import android.os.Handler;

public class ModelRepository {
    public interface LoginCallback {

        void onSuccess();

        void onFailure();
    }

    public static class LoginModel {
        public String name;
        public String pwd;
    }

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    public void login(final LoginModel model, final LoginCallback callback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ("ggg".equals(model.name) && "ggg".equals(model.pwd)) {
                    callback.onSuccess();
                } else {
                    callback.onFailure();
                }
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    public void cancel() {

    }
}
