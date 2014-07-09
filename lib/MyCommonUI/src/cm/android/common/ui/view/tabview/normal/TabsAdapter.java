package cm.android.common.ui.view.tabview.normal;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import cm.android.common.ui.view.tabview.BaseTabManager.OnTabPageChangeListener;

import java.util.ArrayList;

public class TabsAdapter extends android.support.v4.app.FragmentPagerAdapter
		implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

	private static final String KEY_TAB_POSITION = "tab_position";
	private android.support.v4.app.FragmentManager mFragmentManager;

	final class TabInfo {
		private final Class<?> clss;
		private final Bundle args;

		TabInfo(Class<?> _class, int position) {
			clss = _class;
			args = new Bundle();
			args.putInt(KEY_TAB_POSITION, position);
		}

		public int getPosition() {
			return args.getInt(KEY_TAB_POSITION, 0);
		}
	}

	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	ActionBar mMainActionBar;
	Context mContext;
	ViewPager mPager;
	private OnTabPageChangeListener onTabPageChangeListener;
	private int prevPosition;

	// public TabsAdapter(FragmentActivity activity, ViewPager pager) {
	// super(activity.getSupportFragmentManager());
	// mFragmentManager = activity.getSupportFragmentManager();
	// mContext = activity;
	//
	// mMainActionBar = activity.getActionBar();
	// mPager = pager;
	// mPager.setAdapter(this);
	// mPager.setOnPageChangeListener(this);
	// mPager.setOffscreenPageLimit(0);
	// }

	public TabsAdapter(FragmentActivity activity, ViewPager pager,
			OnTabPageChangeListener onTabPageChangeListener) {
		super(activity.getSupportFragmentManager());
		mContext = activity;
		mMainActionBar = activity.getActionBar();
		mPager = pager;
		mPager.setAdapter(this);
		mPager.setOnPageChangeListener(this);
		mPager.setOffscreenPageLimit(0);
		this.onTabPageChangeListener = onTabPageChangeListener;
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		Fragment f = Fragment.instantiate(mContext, info.clss.getName(),
				info.args);
		return f;
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// The parent's setPrimaryItem() also calls setMenuVisibility(), so we
		// want to know
		// when it happens.
		super.setPrimaryItem(container, position, object);
	}

	public void addTab(ActionBar.Tab tab, Class<?> clss, int position) {
		TabInfo info = new TabInfo(clss, position);
		tab.setTag(info);
		tab.setTabListener(this);
		mTabs.add(info);
		mMainActionBar.addTab(tab);
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
		// return PagerAdapter.POSITION_NONE;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// Do nothing
	}

	@Override
	public void onPageSelected(int position) {
		mMainActionBar.setSelectedNavigationItem(position);
		// 发出事件
		if (onTabPageChangeListener != null) {
			Fragment prevFragment = getFragment(mPager, prevPosition);
			Fragment selectedFragment = getFragment(mPager, position);
			onTabPageChangeListener.onPageSelected(position, selectedFragment,
					prevFragment);
		}
		prevPosition = position;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// Do nothing
	}

	@Override
	public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// Do nothing
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		TabInfo info = (TabInfo) tab.getTag();
		mPager.setCurrentItem(info.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// Do nothing

	}

	/**
	 * 根据position获取Fragment
	 * 
	 * @param container
	 * @param position
	 * @return
	 */
	public android.support.v4.app.Fragment getFragment(View container,
			int position) {
		final long itemId = getItemId(position);
		// Do we already have this fragment?
		String name = makeFragmentName(container.getId(), itemId);
		android.support.v4.app.Fragment fragment = mFragmentManager
				.findFragmentByTag(name);
		return fragment;
	}

	/**
	 * @see {link android.support.v4.app.FragmentPagerAdapter}
	 */
	private static String makeFragmentName(int viewId, long id) {
		return "android:switcher:" + viewId + ":" + id;
	}
}
