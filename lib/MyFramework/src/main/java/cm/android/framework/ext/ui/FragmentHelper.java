package cm.android.framework.ext.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * FragmentManager包装类
 */
@TargetApi(11)
public class FragmentHelper {

    private FragmentManager mFragmentManager;

    private static final Logger logger = LoggerFactory.getLogger(FragmentHelper.class);

    public FragmentHelper(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        FragmentManager.enableDebugLogging(false);
    }

    /**
     * BackStack中是否有Fragment存在
     *
     * @return true:历史栈中存在Fragment
     */
    public boolean hasBackStackEntry() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count == 0) {
            return false;
        }
        return true;
    }

    public int getBackStackEntryCount() {
        return mFragmentManager.getBackStackEntryCount();
    }

    /**
     * 获取BackStack中对应位置的Fragment
     */
    public FragmentManager.BackStackEntry getBackStackEntry(int position) {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count == 0) {
            return null;
        } else {
            return mFragmentManager.getBackStackEntryAt(position);
        }
    }

    /************************************************************************************************
     * add 是把一个fragment添加到一个容器 container 里
     * replace 是先remove掉相同id的所有fragment，然后在add当前的这个fragment。
     * 在大部分情况下，这两个的表现基本相同。因为，一般，咱们会使用一个FrameLayout来当容器，
     * 而每个Fragment被add 或者 replace 到这个FrameLayout的时候，都是显示在最上层的。
     * 所以你看到的界面都是一样的。但是，使用add的情况下，这个FrameLayout其实有2层，
     * 多层肯定要比一层的来得浪费，所以还是推荐使用replace。当然有时候还是需要使用add的。
     * 比如要实现轮播图的效果，每个轮播图都是一个独立的Fragment，而他的容器FrameLayout需要add多个Fragment，
     * 这样他就可以根据提供的逻辑进行轮播了。
     * 而至于返回键的时候，这个跟事务有关，跟使用add还是replace没有任何关系。
     ************************************************************************************************/

    /**
     * 替换resId中存在的Fragment
     */
    public void replace(Bundle arguments, int resId, Fragment fragment) {
        String tag = fragment.getClass().getName();
        Fragment preFragment = mFragmentManager.findFragmentByTag(tag);
        removeFragment(preFragment);

        if (null != arguments) {
            fragment.setArguments(arguments);
        }
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(resId, fragment, tag);
        // fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 在resId中添加Fragment
     */
    public void add(Bundle arguments, int resId, Fragment fragment) {
        String tag = fragment.getClass().getName();
        Fragment preFragment = mFragmentManager.findFragmentByTag(tag);
        removeFragment(preFragment);

        if (null != arguments) {
            fragment.setArguments(arguments);
        }
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.add(resId, fragment, tag);
        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(tag);
        // fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 弹出对话框
     */
    public void showDialog(Bundle arguments, DialogFragment dialogFragment) {
        String tag = dialogFragment.getClass().getName();
        Fragment preFragment = mFragmentManager.findFragmentByTag(tag);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (preFragment != null) {
            ft.remove(preFragment);
        }
        // ft.addToBackStack(tag);
        if (null != arguments) {
            dialogFragment.setArguments(arguments);
        }
        dialogFragment.show(ft, tag);
    }

    /**
     * 删除对话框Fragment
     */
    public void removeDialogFragment(DialogFragment dialogFragment) {
        // String backStateName = dialogFragment.getClass().getSimpleName();
        // Fragment mBackStackFragment = mFragmentManager
        // .findFragmentByTag(backStateName);
        // Fragment mBackStackFragment =
        // findFragment(dialogFragment.getClass());
        if (dialogFragment != null && dialogFragment.isVisible()) {
            removeFragment(dialogFragment);
        }
    }

    /**
     * 返回到第一个fragment
     */
    public void popBackStackImmediate(String backStateName) {
        mFragmentManager.popBackStackImmediate(backStateName, 0);
    }

    /**
     * 返回之前的fragment
     */
    public void popBackStack() {
        mFragmentManager.popBackStack();
    }

    /**
     * 删除一个fragment
     */
    public void removeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 通过findFragmentByTag获取Fragment，如果不存在则会根据默认构造创建出Fragment对象
     */
    public Fragment findFragment(
            Class<? extends Fragment> clazz) {
        String tag = clazz.getName();
        Fragment fragment = mFragmentManager
                .findFragmentByTag(tag);
        if (fragment == null) {
            // 创建
            // Fragment.instantiate(context, fname);
            try {
                Fragment f = (Fragment) clazz.newInstance();
                return f;
            } catch (InstantiationException e) {
                logger.error(e.getMessage(), e);
                // throw new java.lang.InstantiationException(
                // "Unable to instantiate fragment "
                // + tag
                // + ": make sure class name exists, is public, and has an"
                // + " empty constructor that is public");
                // throw new InstantiationException(
                // "Unable to instantiate fragment "
                // + tag
                // + ": make sure class name exists, is public, and has an"
                // + " empty constructor that is public", e);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
                // throw new InstantiationException(
                // "Unable to instantiate fragment "
                // + tag
                // + ": make sure class name exists, is public, and has an"
                // + " empty constructor that is public", e);
            }
        }
        return fragment;
    }

    /**
     * 根据xml中设置的ID来查找Fragment
     */
    public Fragment findFragmentById(int resId) {
        Fragment fragment = mFragmentManager.findFragmentById(resId);
        return fragment;
    }

    /**
     * 根据Tag查找Fragment
     */
    public Fragment findFragmentByTag(Class<? extends Fragment> clazz) {
        String tag = clazz.getName();
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        return fragment;
    }

}
