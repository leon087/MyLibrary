package cm.android.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * 电池监听器
 */
public class BatteryReceiver extends BaseBroadcastReceiver {

    private BetteryChangedListener listener;

    public BatteryReceiver(BetteryChangedListener listener) {
        if (listener == null) {
            this.listener = defaultListener;
        } else {
            this.listener = listener;
        }
    }

    private static final BetteryChangedListener defaultListener = new BetteryChangedListener() {
        @Override
        public void betteryChanged(BetteryInfo betteryInfo) {
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            BetteryInfo betteryInfo = new BetteryInfo();
            betteryInfo.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);//API:5
            betteryInfo.health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            betteryInfo.present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
            betteryInfo.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            betteryInfo.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            betteryInfo.icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
            betteryInfo.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            betteryInfo.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            betteryInfo.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            betteryInfo.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

            listener.betteryChanged(betteryInfo);
        }
    }

    @Override
    protected IntentFilter createIntentFilter() {
        IntentFilter intentFilter = super.createIntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        return intentFilter;
    }

    public static interface BetteryChangedListener {
        public void betteryChanged(BetteryInfo betteryInfo);
    }

    public static class BetteryInfo {
        public int status;
        public int health;
        public boolean present;
        public int level;
        public int scale;
        public int icon_small;
        public int plugged;
        public int voltage;
        public int temperature;
        public String technology;
    }

}