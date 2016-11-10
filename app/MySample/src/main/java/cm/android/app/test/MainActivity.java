package cm.android.app.test;

import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.os.Bundle;

import cm.android.app.core.ObjectPool;
import cm.android.app.test.server.TestManager;
import cm.android.framework.client.core.Framework;
import cm.android.framework.client.core.LogUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Framework.get().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Framework.get().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TestManager tm = ObjectPool.getTestManager();
        LogUtil.getLogger().error("gggg test = " + Framework.get().getService("test"));
        LogUtil.getLogger().error("gggg onCreate");
        tm.count();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LoggerFactory.getLogger("ggg").error("ggg onPause");
    }
}
