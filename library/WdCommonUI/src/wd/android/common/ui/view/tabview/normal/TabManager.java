package wd.android.common.ui.view.tabview.normal;

import java.util.Random;

import wd.android.common.ui.view.tabview.BaseTabManager;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

public class TabManager extends BaseTabManager {

	private ActionBar mActionBar;
	private TabsAdapter mTabsAdapter;

	public TabManager(FragmentActivity activity, int selectedTab) {
		super(activity, selectedTab);
	}

	public ViewPager initView(OnTabPageChangeListener onTabPageChangeListener) {
		if (mTabsAdapter == null) {
			mViewPager = new ViewPager(mActivity);
			mViewPager.setId(new Random().nextInt(1000));
			mTabsAdapter = new TabsAdapter(mActivity, mViewPager,
					onTabPageChangeListener);
			createTabs(mSelectedTab);
		}
		mActionBar.setSelectedNavigationItem(mSelectedTab);
		return mViewPager;
	}

	private void createTabs(int selectedIndex) {
		mActionBar = mActivity.getActionBar();
		if (mActionBar != null) {
			// set defaults for logo & home up
			mActionBar.setDisplayHomeAsUpEnabled(false);
			mActionBar.setDisplayUseLogoEnabled(false);
			// 设置AcitonBar的操作模型
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			// 将Activity的头部去掉
			mActionBar.setDisplayShowTitleEnabled(false);
			mActionBar.setDisplayOptions(0);
			for (int i = 0; i < mTabHolders.size(); i++) {
				initTab(i);
			}
			mActionBar.setSelectedNavigationItem(selectedIndex);
		}
	}

	private void initTab(int index) {
		Tab tab = mActionBar.newTab();
		TabHolder tabHolder = (TabHolder) mTabHolders.get(index);

		// 设置icon
		if (tabHolder.iconRes != null) {
			tab.setIcon(tabHolder.iconRes);
		} else if (tabHolder.iconDrawable != null) {
			tab.setIcon(tabHolder.iconDrawable);
		}

		// 设置title和ContentDescription
		if (tabHolder.titleRes != null) {
			tab.setText(tabHolder.titleRes);
			tab.setContentDescription(tabHolder.titleRes);
		} else if (tabHolder.titleCharSequence != null) {
			tab.setText(tabHolder.titleCharSequence);
			tab.setContentDescription(tabHolder.titleCharSequence);
		}

		// 设置自定义view
		if (tabHolder.customView != null) {
			tab.setCustomView(tabHolder.customView);
		} else if (tabHolder.customViewRes != null) {
			tab.setCustomView(tabHolder.customViewRes);
		}

		mTabsAdapter.addTab(tab, tabHolder.fragmentClazz, index);
	}

	public static class TabHolder extends BaseTabHolder {
		// public Class<? extends android.support.v4.app.Fragment> fragmentClazz
		// = null; // Tab
		// public CharSequence titleCharSequence = null;
		public Integer titleRes = null;

		public Drawable iconDrawable = null;
		public Integer iconRes = null;

		public View customView = null;
		public Integer customViewRes = null;
	}
}
