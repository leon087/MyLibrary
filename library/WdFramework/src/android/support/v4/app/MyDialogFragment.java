/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.app;

/**
 * Static library support version of the framework's
 * {@link android.app.DialogFragment}. Used to write apps that run on platforms
 * prior to Android 3.0. When running on Android 3.0 or above, this
 * implementation is still used; it does not try to switch to the framework's
 * implementation. See the framework SDK documentation for a class overview.
 */
public class MyDialogFragment extends DialogFragment {
	/**
	 * Display the dialog, adding the fragment to the given FragmentManager.
	 * This is a convenience for explicitly creating a transaction, adding the
	 * fragment to it with the given tag, and committing it. This does
	 * <em>not</em> add the transaction to the back stack. When the fragment is
	 * dismissed, a new transaction will be executed to remove it from the
	 * activity.
	 * 
	 * @param manager
	 *            The FragmentManager this fragment will be added to.
	 * @param tag
	 *            The tag for this fragment, as per
	 *            {@link FragmentTransaction#add(Fragment, String)
	 *            FragmentTransaction.add}.
	 */
	public void show(FragmentManager manager, String tag) {
		mDismissed = false;
		mShownByMe = true;
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		// ft.commit();
		ft.commitAllowingStateLoss();
	}

	/**
	 * Display the dialog, adding the fragment using an existing transaction and
	 * then committing the transaction.
	 * 
	 * @param transaction
	 *            An existing transaction in which to add the fragment.
	 * @param tag
	 *            The tag for this fragment, as per
	 *            {@link FragmentTransaction#add(Fragment, String)
	 *            FragmentTransaction.add}.
	 * @return Returns the identifier of the committed transaction, as per
	 *         {@link FragmentTransaction#commit() FragmentTransaction.commit()}
	 *         .
	 */
	public int show(FragmentTransaction transaction, String tag) {
		mDismissed = false;
		mShownByMe = true;
		transaction.add(this, tag);
		mViewDestroyed = false;
		// mBackStackId = transaction.commit();
		mBackStackId = transaction.commitAllowingStateLoss();
		return mBackStackId;
	}

	void dismissInternal(boolean allowStateLoss) {
		if (mDismissed) {
			return;
		}
		mDismissed = true;
		mShownByMe = false;
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mViewDestroyed = true;
		if (mBackStackId >= 0) {
			getFragmentManager().popBackStack(mBackStackId,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			mBackStackId = -1;
		} else {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.remove(this);
			if (allowStateLoss) {
				ft.commitAllowingStateLoss();
			} else {
				// ft.commit();
				ft.commitAllowingStateLoss();
			}
		}
	}
}
