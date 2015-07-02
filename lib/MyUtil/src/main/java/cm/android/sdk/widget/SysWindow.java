package cm.android.sdk.widget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SysWindow {

    private static final Logger logger = LoggerFactory.getLogger("SysWindow");

    protected WindowManager.LayoutParams mLayoutParams;

    protected AtomicBoolean show = new AtomicBoolean(false);

    protected View rootView;

    protected WindowManager mWindowManager;

    public SysWindow(Context context) {
        this.mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        this.mLayoutParams = initLayoutParams();

        rootView = onCreateView(context);
    }

    protected abstract View onCreateView(Context context);

    protected WindowManager.LayoutParams initLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;

        return layoutParams;
    }

    public void hide() {
        if (!show.get()) {
            return;
        }

        if (rootView == null) {
            return;
        }

        try {
            this.mWindowManager.removeView(this.rootView);
            show.set(false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public boolean isShow() {
        return this.show.get();
    }

    public void show() {
        if (show.get()) {
            return;
        }

        if (rootView == null) {
            return;
        }

        try {
            this.mWindowManager.addView(this.rootView, this.mLayoutParams);
            show.set(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
