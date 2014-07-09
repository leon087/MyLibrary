package cm.android.common.image;

import android.graphics.Bitmap;
import cm.android.util.BitmapUtil;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class ReflectionDisplayer implements BitmapDisplayer {
	@Override
	public void display(Bitmap bitmap, ImageAware imageAware,
			LoadedFrom loadedFrom) {
		// 解析，此处默认为内存中没有该bitmap对象
		int reHeight = bitmap.getHeight() / 2;
		Bitmap reBitmap = BitmapUtil.createBitmapWithReflection(bitmap,
				reHeight, 0);
		Bitmap rotateBitmap = BitmapUtil.createRotateBitmap(reBitmap, 30);

		imageAware.setImageBitmap(rotateBitmap);
	}
}
