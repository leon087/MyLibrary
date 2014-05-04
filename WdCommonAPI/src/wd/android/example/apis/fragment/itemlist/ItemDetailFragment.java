package wd.android.example.apis.fragment.itemlist;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import wd.android.app.ui.fragment.MyBaseFragment;
import wd.android.example.apis.fragment.itemlist.dummy.DummyContent;
import wd.android.wdcommondapi.R;

public class ItemDetailFragment extends MyBaseFragment {
    public static final String ARG_ITEM_ID = "item_id";
    private DummyContent.DummyItem mItem;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
                    ARG_ITEM_ID));
        }
    }

    @Override
    public void initView(View rootView, Bundle bundle) {
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail))
                    .setText(mItem.content);
        }
    }

    @Override
    public void initData(Bundle bundle) {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_item_detail;
    }

}
