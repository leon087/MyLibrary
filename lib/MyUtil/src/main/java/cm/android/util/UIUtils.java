package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.text.TextPaint;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * UI Util类
 */
public class UIUtils {

    private static Toast mToast = null;

    private static final Logger logger = LoggerFactory.getLogger(UIUtils.class);

    public static void cancel() {
        if (null != mToast) {
            mToast.cancel();
        }
    }

    public static void setBoldText(TextView textView) {
        TextPaint tpaint = textView.getPaint();
        tpaint.setFakeBoldText(true);
    }

    public static void showToast(Context context, int resId) {
        mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showToast(Context context, CharSequence msg) {
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 自适应高度
     */
    public static int getHeightSpec(View view, int heightMeasureSpec) {
        int heightSpec = 0;
        if (view.getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }
        return heightSpec;
    }

    public static final <E extends View> E findView(Activity activity, int id) {
        return findView(activity.getWindow().getDecorView(), id);
    }

    @SuppressWarnings("unchecked")
    public static final <E extends View> E findView(View view, int id) {
        try {
            return (E) view.findViewById(id);
        } catch (ClassCastException e) {
            logger.error("Could not cast View to concrete class.", e);
            throw e;
        }
    }

    /**
     * 获取Activity中的rootView
     */
    public static View getContentView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content))
                .getChildAt(0);
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity ctx) {
        int width;
        Display display = ctx.getWindowManager().getDefaultDisplay();
        try {
            Method mGetRawW = Display.class.getMethod("getRawWidth");
            width = (Integer) mGetRawW.invoke(display);
        } catch (Exception e) {
            width = display.getWidth();
        }
        return width;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Activity ctx) {
        int height;
        Display display = ctx.getWindowManager().getDefaultDisplay();
        try {
            Method mGetRawH = Display.class.getMethod("getRawHeight");
            height = (Integer) mGetRawH.invoke(display);
        } catch (Exception e) {
            height = display.getHeight();
        }
        return height;
    }
}
