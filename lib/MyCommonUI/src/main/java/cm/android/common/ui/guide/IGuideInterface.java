package cm.android.common.ui.guide;

import android.graphics.drawable.BitmapDrawable;

/**
 * 引导图操作接口
 */
public interface IGuideInterface {
    /**
     * 显示引导图
     *
     * @param bitmapDrawable
     */
    void onShowGuide(BitmapDrawable bitmapDrawable);

    /**
     * 获取引导图
     *
     * @return
     */
    BitmapDrawable getGuide();

    /**
     * 判断是否允许显示引导图
     *
     * @return
     */
    boolean canShowGuide();
}
