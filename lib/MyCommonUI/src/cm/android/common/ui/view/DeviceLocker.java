package cm.android.common.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import cm.android.util.EnvironmentInfo;

public class DeviceLocker {
    private WindowManager mWindowManager;
    private View mLockView;
    private LayoutParams mLockViewLayoutParams;
    private boolean isLocked = false;

    public DeviceLocker(Context context, View lockView) {
        lockView.setBackgroundColor(context.getResources().getColor(
                android.R.color.holo_blue_dark));
        initView(lockView);
        mLockViewLayoutParams = initLayoutParams();
        mWindowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
    }

    private void initView(View lockView) {
        mLockView = lockView;
        lockView.setFocusable(true);
        setViewActivated(lockView);
        lockView.setClickable(true);
    }

    @TargetApi(11)
    private void setViewActivated(View view) {
        if (EnvironmentInfo.SdkUtil.hasHoneycomb()) {
            view.setActivated(true);
        }
    }

    private WindowManager.LayoutParams initLayoutParams() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        // DeviceLockView myFV = new DeviceLockView(activity, wmParams);
        // 设置LayoutParams(全局变量）相关参数
        // wmParams = wmParams.getMywmParams();
        wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
        // | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // wmParams.flags = 32;
        // 下面的flags属性效果形同“锁定”，即不可触摸，不接受任何事件，同时不影响后面的事件响应。
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_NOT_TOUCHABLE
                | LayoutParams.FLAG_FULLSCREEN
                | LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至左上角
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        // DisplayMetrics dm = new DisplayMetrics();
        // ((Activity) activity).getWindowManager().getDefaultDisplay()
        // .getMetrics(dm);

        // 设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.MATCH_PARENT;
        wmParams.height = LayoutParams.MATCH_PARENT;
        // wmParams.width = dm.widthPixels;
        // wmParams.height = dm.heightPixels;

        // 显示myFloatView图像
        // wm.addView(lockView, wmParams);

        return wmParams;
    }

    public synchronized void lock() {
        if (!isLocked) {
            mWindowManager.addView(mLockView, mLockViewLayoutParams);
        }
        isLocked = true;
    }

    public synchronized void unlock() {
        if (isLocked) {
            mWindowManager.removeView(mLockView);
        }
        isLocked = false;
    }

    public static class DeviceLockView extends ImageView {

        public DeviceLockView(Context context) {
            super(context);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            return true;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            return true;
            // return super.dispatchKeyEvent(event);
        }

    }
}
