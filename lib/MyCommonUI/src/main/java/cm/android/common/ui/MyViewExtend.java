package cm.android.common.ui;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyViewExtend {

    public static interface IRefreshListener {

        public static final int LOAD_PREV = 0x01;
        public static final int LOAD_NEXT = 0x02;

        boolean onRefresh(int type);
    }

    public static interface OnAtEndListener {

        public static final int TYPE_KEY_UP = 0x0001;
        public static final int TYPE_KEY_DOWN = 0x0010;
        public static final int TYPE_KEY_LEFT = 0x0100;
        public static final int TYPE_KEY_RIGHT = 0x1000;

        boolean onAtEnd(int type);
    }

    public static interface OnItemCheckedListener {

        void onItemChecked(AdapterView<?> adapterView, View view, int position,
                boolean checked);
    }

    public static abstract class MyOnItemSelectedListener implements
            OnItemSelectedListener, OnItemCheckedListener {

        private int last = -1;

        private View lastView = null;

        @Override
        public final void onItemSelected(AdapterView<?> adapterView, View view,
                int position, long id) {
            if (last != -1) {
                onItemChecked(adapterView, lastView, last, false);
            }
            onItemChecked(adapterView, view, position, true);
            lastView = view;
            last = position;
        }

        @Override
        public final void onNothingSelected(AdapterView<?> adapterView) {
            if (last != -1) {
                onItemChecked(adapterView, lastView, last, false);
            }
        }
    }

    public static class MyOnFocusChangeListener implements
            OnFocusChangeListener {

        private OnFocusChangeListener listener;

        private AdapterView<?> adapterView;

        public MyOnFocusChangeListener(AdapterView<?> adapterView,
                OnFocusChangeListener listener) {
            this.listener = listener;
            this.adapterView = adapterView;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                adapterView.setSelection(-1);
            }
            if (null != listener) {
                listener.onFocusChange(v, hasFocus);
            }
        }
    }
}
