package wd.android.example.apis.fragment.itemlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

import wd.android.app.ui.activity.MyBaseActivity;
import wd.android.wdcommondapi.R;

public class ItemDetailActivity extends MyBaseActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this,
                    new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBeforeContentView(Bundle savedInstanceState) {
        //super.onBeforeContentView(savedInstanceState);
        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void initView(View rootView, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, getIntent()
                    .getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment).commit();
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getRootViewId() {
        return R.layout.activity_item_detail;
    }
}
