package cm.android.common.ui.view.tabview;

import android.support.v4.app.FragmentActivity;

import cm.android.common.ui.view.tabview.normal.TabManager;
import cm.android.common.ui.view.tabview.slidetabview.SlideTabManager;

public class TabFactory {

    public static <T> T createTabManager(Class<T> clazz,
            FragmentActivity activity, int selectedTab) {
        if (clazz.isAssignableFrom(TabManager.class)) {
            return (T) new TabManager(activity, selectedTab);
        } else if (clazz.isAssignableFrom(SlideTabManager.class)) {
            return (T) new SlideTabManager(activity, selectedTab);
        }
        return null;
    }
}
