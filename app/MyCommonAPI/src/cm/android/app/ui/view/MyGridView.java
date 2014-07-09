package cm.android.app.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import cm.android.util.util.UIUtils;

/**
 * 创建一个可以自适配高度的ListView
 * 
 */
public class MyGridView extends GridView {

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = UIUtils.getHeightSpec(this, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightSpec);
	}

}
