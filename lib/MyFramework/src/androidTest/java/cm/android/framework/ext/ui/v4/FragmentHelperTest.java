package cm.android.framework.ext.ui.v4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.test.InstrumentationTestCase;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;


public class FragmentHelperTest extends InstrumentationTestCase {

    FragmentManager mFragmentManager = new FragmentManager() {
        @Override
        public FragmentTransaction beginTransaction() {
            return null;
        }

        @Override
        public boolean executePendingTransactions() {
            return false;
        }

        @Override
        public Fragment findFragmentById(int id) {
            return null;
        }

        @Override
        public Fragment findFragmentByTag(String tag) {
            return null;
        }

        @Override
        public void popBackStack() {

        }

        @Override
        public boolean popBackStackImmediate() {
            return false;
        }

        @Override
        public void popBackStack(String name, int flags) {

        }

        @Override
        public boolean popBackStackImmediate(String name, int flags) {
            return false;
        }

        @Override
        public void popBackStack(int id, int flags) {

        }

        @Override
        public boolean popBackStackImmediate(int id, int flags) {
            return true;
        }

        @Override
        public int getBackStackEntryCount() {
            return 1;
        }

        @Override
        public BackStackEntry getBackStackEntryAt(int index) {
            return null;
        }

        @Override
        public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {

        }

        @Override
        public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {

        }

        @Override
        public void putFragment(Bundle bundle, String key, Fragment fragment) {

        }

        @Override
        public Fragment getFragment(Bundle bundle, String key) {
            return null;
        }

        @Override
        public List<Fragment> getFragments() {
            return null;
        }

        @Override
        public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
            return null;
        }

        @Override
        public boolean isDestroyed() {
            return false;
        }

        @Override
        public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

        }
    };

    public void testHasBackStackEntry() throws Exception {
        int count = mFragmentManager.getBackStackEntryCount();
        boolean result = count != 0;
        assertEquals(true, result);
    }

    public void testGetBackStackEntry() throws Exception {
        int count = mFragmentManager.getBackStackEntryCount();
        boolean result = count == 0;
        boolean result2 = mFragmentManager.getBackStackEntryAt(1) == null;
        if (result) {
            assertEquals(true, result);
        } else {
            assertEquals(true, result2);
        }
    }
}
