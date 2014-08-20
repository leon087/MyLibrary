package cm.android.sdk.widget.adapter;

import android.content.Context;
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
        AdapterViewHolder viewHolder;
        if (null == convertView) {
            convertView = createRootView(position, parent);
            viewHolder = initViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AdapterViewHolder) convertView.getTag();
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

    /**
     * 根据ViewHolder刷新数据
     *
     * @param viewHolder
     * @param position
     * @param data
     */
    protected abstract void updateView(AdapterViewHolder viewHolder,
                                       int position, T data);

    /**
     * 初始化AdapterViewHolder
     *
     * @param convertView
     * @return
     */
    protected abstract AdapterViewHolder initViewHolder(View convertView);

    /**
     * 创建RootView
     *
     * @return
     */
    protected abstract View createRootView(int position, ViewGroup parent);

    /**
     * 根据layoutResId创建View
     *
     * @param layoutResId
     * @return
     */
    protected View createRootView(int layoutResId) {
        return mInflater.inflate(layoutResId, null);
    }

    /**
     * AdapterViewHolder
     */
    public static class AdapterViewHolder {

    }

}
