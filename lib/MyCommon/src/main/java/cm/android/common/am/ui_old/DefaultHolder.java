
package cm.android.common.am.ui_old;

import android.app.Application;
import android.widget.AbsListView;

public class DefaultHolder {
    public int filterMode = BaseAppAdapter.FILTER_APPS_THIRD_PARTY_EXCLUDE_SELF;
    public ApplicationsState applicationsState;
    // sort order
    public int mSortOrder = BaseAppAdapter.SORT_ORDER_ALPHA;

    private BaseAppAdapter adapter;
    private AbsListView listView;

    public void init(Application application) {
        applicationsState = ApplicationsState.getInstance(application);
    }

    public void initView(BaseAppAdapter adapter, AbsListView listView) {
        // adapter = new AppAdapter(this.getActivity(), applicationsState,
        // filterMode);
        this.adapter = adapter;
        listView.setAdapter(adapter);
        listView.setRecyclerListener(adapter);
    }

    public void resume() {
        if (adapter != null) {
            adapter.resume(mSortOrder);
        }
        // if (mRunningProcessesView != null) {
        // boolean haveData = mRunningProcessesView.doResume(mOwner,
        // mRunningProcessesAvail);
        // if (haveData) {
        // mRunningProcessesView.setVisibility(View.VISIBLE);
        // mLoadingContainer.setVisibility(View.INVISIBLE);
        // } else {
        // mLoadingContainer.setVisibility(View.VISIBLE);
        // }
        // }
    }

    public void pause() {
        if (adapter != null) {
            adapter.pause();
        }
        // if (mRunningProcessesView != null) {
        // mRunningProcessesView.doPause();
        // }
    }
}
