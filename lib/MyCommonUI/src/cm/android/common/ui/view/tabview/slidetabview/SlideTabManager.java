package cm.android.common.ui.view.tabview.slidetabview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import cm.android.common.ui.view.tabview.BaseTabManager;

public class SlideTabManager extends BaseTabManager {
	// private ViewPager mViewPager;
	// private FragmentActivity mActivity;
	// private List<BaseTabHolder> mTabHolders;

	public SlideTabManager(FragmentActivity mActivity, int selectedTab) {
		super(mActivity, selectedTab);
		this.mActivity = mActivity;
		// mTabHolders = new ArrayList<SlideTabManager.SlideTabHolder>();
	}

	public ViewPager initView(ViewPager viewPager,
			OnTabPageChangeListener onTabPageChangeListener) {
		mViewPager = viewPager;
		// mViewPager = new ViewPager(mActivity);
		// PagerTabStrip pagerTabStrip = new PagerTabStrip(mActivity);
		// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// // 此处相当于布局文件中的Android:layout_gravity属性
		// lp.gravity = Gravity.TOP;
		// pagerTabStrip.setLayoutParams(lp);
		// mViewPager.addView(pagerTabStrip);
		// mViewPager.setId(new Random().nextInt(1000));
		mViewPager.setAdapter(new SlideTabPagerAdapter(mActivity, mTabHolders));
		mViewPager.setCurrentItem(mSelectedTab);
		MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener(
				mSelectedTab, onTabPageChangeListener);
		myOnPageChangeListener.onPageSelected(mSelectedTab);
		mViewPager.setOnPageChangeListener(myOnPageChangeListener);
		return mViewPager;
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {
		private int prevPosition;
		private OnTabPageChangeListener onTabPageChangeListener;

		MyOnPageChangeListener(int position,
				OnTabPageChangeListener onTabPageChangeListener) {
			prevPosition = position;
			this.onTabPageChangeListener = onTabPageChangeListener;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// This space for rent
		}

		@Override
		public void onPageSelected(int position) {
			// This space for rent
			if (onTabPageChangeListener != null) {
				SlideTabPagerAdapter adapter = (SlideTabPagerAdapter) mViewPager
						.getAdapter();
				Fragment prevFragment = adapter.getFragment(mViewPager,
						prevPosition);
				Fragment selectedFragment = adapter.getFragment(mViewPager,
						position);
				onTabPageChangeListener.onPageSelected(position,
						selectedFragment, prevFragment);
			}
			prevPosition = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// This space for rent
		}
	}

	public static class SlideTabHolder extends BaseTabHolder {
		// public android.support.v4.app.Fragment fragment;
		// public Class<? extends android.support.v4.app.Fragment> fragmentClazz
		// = null; // Tab
	}
}
