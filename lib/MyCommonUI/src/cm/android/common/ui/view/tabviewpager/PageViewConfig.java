package cm.android.common.ui.view.tabviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class PageViewConfig {
	protected List<View> listViews; // Tab页面列表
	protected List<String> titles;
	protected Context mContext;
	protected int count;

	protected android.support.v4.view.ViewPager mViewPager;
	protected RadioGroup tabGroup;
	private View rootView;

	public PageViewConfig(Context context, int count) {
		mContext = context;
		titles = new ArrayList<String>();
		listViews = new ArrayList<View>();
		this.count = count;

		rootView = initViews();
	}

	public void initTitles(List<String> titles) {
		this.titles.addAll(titles);
	}

	public final List<View> getContentView() {
		return listViews;
	}

	public final int getCount() {
		return count;
	}

	public final ViewPager getViewPager() {
		return mViewPager;
	}

	public final RadioGroup getTabGroup() {
		return tabGroup;
	}

	public View getRootView() {
		return rootView;
	}

	public abstract View initTabView(int index);

	public abstract View initViews();
}
