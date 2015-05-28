package cm.android.app.test;

import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cm.android.app.core.MyManager;
import cm.android.framework.core.ServiceManager;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServiceManager.start(new ServiceManager.InitListener() {
            @Override
            public void initSucceed() {
                LoggerFactory.getLogger("ggg").error("ggg testService1 initSucceed");
                startService(new Intent(MyManager.getAppContext(), TestService1.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceManager.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LoggerFactory.getLogger("ggg").error("ggg onPause");
    }
}
