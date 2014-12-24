package cm.android.common.ui.view.tabviewpager;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PageViewManager {

    private ViewPager mViewPager;

    private MyPagerAdapter adapter;

    private RadioGroup tabGroup;

    private OnPageListener onPageChangeListener;

    private OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            uiHandler.sendEmptyMessage(v.getId());
            // 转圈
        }
    };

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            mViewPager.setCurrentItem(msg.what, true);
            // 转圈消失
            onPageChangeListener.onPageSelected(msg.what, true);
        }
    };

    public PageViewManager(OnPageListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void initPageView(PageViewConfig pageViewConfig) {
        mViewPager = pageViewConfig.getViewPager();
        tabGroup = pageViewConfig.getTabGroup();
        tabGroup.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams layoutParams = new RadioGroup.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f);

        RadioButton tabBtn = null;
        for (int i = 0; i < pageViewConfig.getCount(); i++) {
            tabBtn = (RadioButton) pageViewConfig.initTabView(i);
            tabBtn.setOnClickListener(onClickListener);
            tabBtn.setId(i);
            tabGroup.addView(tabBtn, layoutParams);
        }

        adapter = new MyPagerAdapter(pageViewConfig.getContentView());
        mViewPager.setAdapter(adapter);
        MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
        mViewPager.setOnPageChangeListener(myOnPageChangeListener);

        tabGroup.check(0);
        mViewPager.setCurrentItem(0);
        myOnPageChangeListener.onPageSelected(0);
    }

    public interface OnPageListener {

        public void onPageSelected(int position, boolean refresh);
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            if (tabGroup.getCheckedRadioButtonId() != position) {
                tabGroup.check(position);
            }
            onPageChangeListener.onPageSelected(position, false);
        }
    }
}
