package wd.android.common.ui.view.tabview;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public abstract class BaseTabManager {
	protected ViewPager mViewPager;
	protected FragmentActivity mActivity;
	protected int mSelectedTab = 0;

	protected List<BaseTabHolder> mTabHolders;

	public BaseTabManager(FragmentActivity mActivity, int selectedTab) {
		this.mActivity = mActivity;
		mTabHolders = new ArrayList<BaseTabHolder>();
		this.mSelectedTab = selectedTab;
	}

	// public abstract ViewPager initView();

	public void addTabHolder(BaseTabHolder tabHolder) {
		mTabHolders.add(tabHolder);
	}

	public static class BaseTabHolder {
		public Class<? extends android.support.v4.app.Fragment> fragmentClazz = null; // Tab
		public CharSequence titleCharSequence;
	}

	public static interface OnTabPageChangeListener {
		public void onPageSelected(int selectedPosition,
				android.support.v4.app.Fragment selectedFragment,
				android.support.v4.app.Fragment prevFragment);
	}
}
