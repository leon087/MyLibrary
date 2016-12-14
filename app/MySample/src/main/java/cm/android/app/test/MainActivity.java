package cm.android.app.test;

import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import cm.android.app.core.ObjectPool;
import cm.android.app.test.server.TestManager;
import cm.android.framework.client.core.Framework;
import cm.android.framework.client.core.LogUtil;
import cm.android.sdk.content.BaseBroadcastReceiver;

public class MainActivity extends Activity {

    private BaseBroadcastReceiver receiver = new BaseBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            android.util.Log.e("gggg", "ggggg action = " + intent.getAction());
        }

        @Override
        public IntentFilter createIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction("gggg.action.timer");
            return filter;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.getLogger().debug("gggggggg onCreate");
        LogUtil.getLogger().trace("gggggggg onCreate");

        Framework.get().start();
        receiver.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.getLogger().debug("gggggggg onDestroy");
        LogUtil.getLogger().trace("gggggggg onDestroy");
        receiver.unregister(this);
        Framework.get().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TestManager tm = ObjectPool.getTestManager();
        LogUtil.getLogger().error("gggg onCreate");
        tm.count();

        ObjectPool.getTimerManager().register("gggg.action.timer", 1000, 1000);

//        Intent intent = new Intent("cm.mdm.android.intent.action.WAKEUP_MDM");
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//        this.sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LoggerFactory.getLogger("ggg").error("ggg onPause");
        ObjectPool.getTimerManager().unregister("gggg.action.timer");
    }
}
