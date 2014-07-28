package cm.android.common.ui.view.tabview.slidetabview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import cm.android.common.ui.view.tabview.BaseTabManager.BaseTabHolder;
import cm.android.common.ui.view.tabview.slidetabview.SlideTabManager.SlideTabHolder;

import java.util.List;

public class SlideTabPagerAdapter extends
        android.support.v4.app.FragmentPagerAdapter {

    private List<BaseTabHolder> holders;
    private android.support.v4.app.FragmentManager mFragmentManager;
    private FragmentActivity mActivity;

    public SlideTabPagerAdapter(FragmentActivity activity,
                                List<BaseTabHolder> holders) {
        super(activity.getSupportFragmentManager());
        mActivity = activity;
        mFragmentManager = activity.getSupportFragmentManager();
        this.holders = holders;
    }

    /**
     * @see {@link android.support.v4.app.FragmentPagerAdapter}
     */
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        position = position % holders.size();
        Fragment f = Fragment.instantiate(mActivity, ((SlideTabHolder) holders
                .get(position)).fragmentClazz.getName(), null);
        // return ((SlideTabHolder) holders.get(position)).fragment;
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        position = position % holders.size();
        return ((SlideTabHolder) holders.get(position)).titleCharSequence;
    }

    @Override
    public int getCount() {
        return holders.size();
        // return Integer.MAX_VALUE;
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
        position = position % holders.size();
        final long itemId = getItemId(position);
        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        android.support.v4.app.Fragment fragment = mFragmentManager
                .findFragmentByTag(name);
        return fragment;
    }
}
