package cm.android.sdk.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class SysWindow {

    private static final Logger logger = LoggerFactory.getLogger("SysWindow");

    protected Context mContext;

    protected WindowManager.LayoutParams mLayoutParams;

    protected boolean mShow = false;

    protected View mView;

    protected WindowManager mWindowManager;

    public SysWindow(Context paramContext) {
        this.mContext = paramContext;
        this.mWindowManager = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE));
        this.mLayoutParams = new WindowManager.LayoutParams();
        this.mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        this.mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        this.mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.mLayoutParams.gravity = Gravity.CENTER;
    }

    public void hide() {
        if (!mShow) {
            return;
        }

        if (mView == null) {
            return;
        }

        if (mWindowManager == null) {
            return;
        }

        try {
            this.mWindowManager.removeView(this.mView);
            this.mShow = false;
            return;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public boolean isShow() {
        return this.mShow;
    }

    public void show() {
        if (mShow) {
            return;
        }

        if (mView == null) {
            return;
        }

        if (mWindowManager == null) {
            return;
        }

        if (mLayoutParams == null) {
            return;
        }

        try {
            this.mWindowManager.addView(this.mView, this.mLayoutParams);
            this.mShow = true;
            return;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
