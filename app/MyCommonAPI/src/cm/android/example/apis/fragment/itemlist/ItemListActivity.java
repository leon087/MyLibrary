package cm.android.example.apis.fragment.itemlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cm.android.app.ui.activity.MyBaseActivity;
import cm.android.wdcommondapi.R;

public class ItemListActivity extends MyBaseActivity implements
        ItemListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onBeforeContentView(Bundle savedInstanceState) {
        //super.onBeforeContentView(savedInstanceState);
    }


    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager().findFragmentById(
                    R.id.item_list)).setActivateOnItemClick(true);
        }
        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getRootViewId() {
        return R.layout.activity_item_list;
    }
}
