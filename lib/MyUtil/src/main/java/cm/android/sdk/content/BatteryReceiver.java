package cm.android.sdk.content;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * 电池监听器
 */
public class BatteryReceiver extends BaseBroadcastReceiver {

    private BatteryChangedListener listener;

    public BatteryReceiver(BatteryChangedListener listener) {
        if (listener == null) {
            this.listener = defaultListener;
        } else {
            this.listener = listener;
        }
    }

    private static final BatteryChangedListener defaultListener = new BatteryChangedListener() {
        @Override
        public void batteryChanged(BatteryInfo batteryInfo) {
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        BatteryInfo betteryInfo = new BatteryInfo();
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

        listener.batteryChanged(betteryInfo);
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter intentFilter = super.createIntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        return intentFilter;
    }

    public static interface BatteryChangedListener {

        public void batteryChanged(BatteryInfo batteryInfo);
    }

    public static class BatteryInfo {

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

        @Override
        public String toString() {
            return "BatteryInfo{" +
                    "status=" + status +
                    ", health=" + health +
                    ", present=" + present +
                    ", level=" + level +
                    ", scale=" + scale +
                    ", icon_small=" + icon_small +
                    ", plugged=" + plugged +
                    ", voltage=" + voltage +
                    ", temperature=" + temperature +
                    ", technology='" + technology + '\'' +
                    '}';
        }
    }

}