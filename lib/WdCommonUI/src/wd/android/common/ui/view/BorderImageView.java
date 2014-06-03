package wd.android.common.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BorderImageView extends ImageView {
	private String namespace = "http://schemas.nbtstatx.com/android";
	private int color = 0;
	private float borderWidth = 0;

	public BorderImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public BorderImageView(Context context) {
		super(context);
	}

	public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		String colorStr = attrs.getAttributeValue(namespace, "borderColor");
		if (null != colorStr && !"".equals(color)) {
			try {
				color = Color.parseColor(colorStr);
			} catch (Exception e) {
			}
		}

		String borderWidthStr = attrs.getAttributeValue(namespace,
				"borderWidth");
		if (null != borderWidthStr && !"".equals(borderWidthStr)) {
			try {
				borderWidth = Float.parseFloat(borderWidthStr);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
	}

	// 设置颜色
	public void setColour(int color) {
		this.color = color;
	}

	// 设置边框宽度
	public void setBorderWidth(float width) {
		borderWidth = width;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画边框
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		if (borderWidth > 0) {
			paint.setStrokeWidth(borderWidth);
			paint.setColor(color);
		}

		Rect rec = canvas.getClipBounds();
		rec.bottom--;
		rec.right--;
		canvas.drawRect(rec, paint);
	}
}
