package cm.android.common.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AsyncImageView extends ImageView {

    private String url;

    public AsyncImageView(Context context) {
        super(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Notifies the drawable that it's displayed state has changed.
     */
    private static void notifyDrawable(Drawable drawable,
            final boolean isDisplayed) {
        // if (drawable instanceof RecyclingBitmapDrawable) {
        // // The drawable is a CountingBitmapDrawable, so notify it
        // ((RecyclingBitmapDrawable) drawable).setIsDisplayed(isDisplayed);
        // } else if (drawable instanceof LayerDrawable) {
        // // The drawable is a LayerDrawable, so recurse on each layer
        // LayerDrawable layerDrawable = (LayerDrawable) drawable;
        // for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
        // notifyDrawable(layerDrawable.getDrawable(i), isDisplayed);
        // }
        // }
    }

    public void setRecycled(boolean isRecycle) {
        // this.isRecycle = isRecycle;
    }

    /**
     * @see android.widget.ImageView#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        // This has been detached from Window, so clear the drawable
        setImageDrawable(null);

        super.onDetachedFromWindow();
    }

    /**
     * @see android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        // Keep hold of previous Drawable
        final Drawable previousDrawable = getDrawable();

        // Call super to set new Drawable
        super.setImageDrawable(drawable);

        // Notify new Drawable that it is being displayed
        notifyDrawable(drawable, true);

        // Notify old Drawable so it is no longer being displayed
        notifyDrawable(previousDrawable, false);
    }

    /**
     * 设置地址
     */
    public void setImageUrl(String url) {
        // MyServiceManager.getAsyncImageManager().setImageUrl(this, url);
        // MyServiceManager.getImageManager().loadImage(url, this);
    }
}
