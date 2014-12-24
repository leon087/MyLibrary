package cm.android.sdk.widget.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;

public abstract class MyBaseAdapter<T> extends AbstractAdapter<T> implements
        AbsListView.RecyclerListener {

    protected final ArrayList<View> mActive = new ArrayList<View>();

    public boolean isRepeat = false;

    public MyBaseAdapter(Context context) {
        super(context);
    }

    /**
     * 设置是否可以循环滚动
     *
     * @param isRepeat true:可以循环滚动，此时getCount()返回Integer.MAX_VALUE
     */
    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    @Override
    public final int getCount() {
        if (isRepeat) {
            return Integer.MAX_VALUE;
        }
        return super.getCount();
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        T data = getItem(position);
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = createRootView(position, parent);
            viewHolder = initView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        updateView(viewHolder, position, data);
        mActive.remove(convertView);
        mActive.add(convertView);
        return convertView;
    }

    @Override
    public void onMovedToScrapHeap(View view) {
        mActive.remove(view);
    }

    protected ViewHolder initView(View convertView) {
        return new DefaultViewHolder(convertView);
    }

    /**
     * 根据ViewHolder刷新数据
     */
    protected abstract void updateView(ViewHolder viewHolder, int position, T data);

    /**
     * 创建RootView
     */
    protected abstract View createRootView(int position, ViewGroup parent);

    /**
     * 根据layoutResId创建View
     */
    protected View createRootView(int layoutResId) {
        return mInflater.inflate(layoutResId, null);
    }

    /**
     * AdapterViewHolder
     */
    public static abstract class ViewHolder {

    }

    public static class DefaultViewHolder extends ViewHolder {

        private SparseArray<View> views = new SparseArray<View>();

        private View convertView;

        public DefaultViewHolder(View convertView) {
            this.convertView = convertView;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = convertView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }
    }

}
