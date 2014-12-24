package cm.android.common.ui.view.tabviewpager;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    public List<View> mListViews;

    public MyPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public void destroyItem(View view, int position, Object arg2) {
        ((ViewPager) view).removeView(mListViews.get(position));
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public Object instantiateItem(View view, int position) {
        ((ViewPager) view).addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
