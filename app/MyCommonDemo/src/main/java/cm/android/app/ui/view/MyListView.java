package cm.android.app.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import cm.android.util.UIUtils;

/**
 * 创建一个可以自适配高度的ListView
 * 
 */
public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpec = UIUtils.getHeightSpec(this, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightSpec);
	}
}
