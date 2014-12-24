package cm.android.common.ui.guide;

import android.graphics.drawable.BitmapDrawable;

/**
 * 引导图Util类
 */
public class GuideUtil {

    /**
     * 显示引导图
     */
    public static void showGuide(IGuideInterface guideInterface) {
        if (!guideInterface.canShowGuide()) {
            return;
        }

        BitmapDrawable bitmapDrawable = guideInterface.getGuide();
        if (null == bitmapDrawable) {
            return;
        }
        guideInterface.onShowGuide(bitmapDrawable);
    }
}
