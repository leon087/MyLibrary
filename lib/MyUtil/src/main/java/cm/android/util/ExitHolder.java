package cm.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import cm.android.sdk.content.BaseBroadcastReceiver;

public class ExitHolder {

    public static final String ACTION_EXIT_ACTIVITY = "cm.android.intent.action.EXIT_ACTIVITY";

    private IExitActivity iExitActivity;

    private ExitReceiver exitReceiver = new ExitReceiver();

    public void init(Context context, IExitActivity iExitActivity) {
        exitReceiver.registerLocal(context);
        this.iExitActivity = iExitActivity;
    }

    public void deInit(Context context) {
        this.iExitActivity = null;
        exitReceiver.unregisterLocal(context);
    }

    public ExitHolder() {

    }

    private class ExitReceiver extends BaseBroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (iExitActivity != null) {
                iExitActivity.exitActivity();
            }
        }

        @Override
        public IntentFilter createIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_EXIT_ACTIVITY);
            return filter;
        }

    }

    public static interface IExitActivity {

        void exitActivity();
    }
}
