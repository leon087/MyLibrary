package cm.android.common.ui.view.tabview.normal;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

//import android.support.v4.app.Fragment;

//import android.support.v4.app.FragmentTransaction;

public class TabsAdapterNew extends FragmentPagerAdapter implements
        ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private static final String KEY_TAB_POSITION = "tab_position";
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    ActionBar mMainActionBar;
    Context mContext;
    ViewPager mPager;
    public TabsAdapterNew(Activity activity, ViewPager pager) {
        super(activity.getFragmentManager());
        mContext = activity;
        mMainActionBar = activity.getActionBar();
        mPager = pager;
        mPager.setAdapter(this);
        mPager.setOnPageChangeListener(this);
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

    public void addTab(ActionBar.Tab tab, Class<?> clss, int position) {
        TabInfo info = new TabInfo(clss, position);
        tab.setTag(info);
        tab.setTabListener(this);
        mTabs.add(info);
        mMainActionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // Do nothing
    }

    @Override
    public void onPageSelected(int position) {
        mMainActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Do nothing
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        // Do nothing
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        TabInfo info = (TabInfo) tab.getTag();
        mPager.setCurrentItem(info.getPosition());
    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
        // Do nothing

    }

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
}
