package cm.android.sdk.content;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sdcard监听类
 */
public class ExternalStorageReceiver extends BaseBroadcastReceiver {
    private static final Logger logger = LoggerFactory.getLogger(ExternalStorageReceiver.class);
    private ExternalStorageListener externalStorageListener;

    /**
     * 初始化
     */
    public ExternalStorageReceiver(Context context,
                                   ExternalStorageListener listener) {
        externalStorageListener = listener;
        register(context);
    }

    /**
     * 释放资源
     */
    public void release() {
        unregister();
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter intentFilter = super.createIntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        intentFilter.addDataScheme("file");
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.info("intent = " + intent);
        String action = intent.getAction();

        if (Intent.ACTION_MEDIA_MOUNTED.equals(action)
                || Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)
                || Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
            // SD卡成功挂载
            externalStorageListener.onMediaMounted();
        } else if (Intent.ACTION_MEDIA_REMOVED.equals(action)
                || Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
                || Intent.ACTION_MEDIA_BAD_REMOVAL.equals(action)) {
            // SD卡挂载失败
            externalStorageListener.onMediaRemoved();
        }
    }

    /**
     * sdcard监听接口
     */
    public interface ExternalStorageListener {
        /**
         * sdcard已挂载
         */
        void onMediaMounted();

        /**
         * sdcard已被移除
         */
        void onMediaRemoved();
    }
}