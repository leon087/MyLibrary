package cm.android.sdk.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

import cm.android.util.ObjectUtil;

/**
 * @desc : 抽象Adapter类
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {

    /**
     * 数据缓存
     */
    protected final List<T> mDataCache = ObjectUtil.newArrayList();

    /**
     * 用于从XML文件中创建Layout
     */
    protected LayoutInflater mInflater;

    protected Context context;

    /**
     * </br><b>description : </b> 创建Adapter，需要给定View创建接口。
     */
    public AbstractAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    /**
     * <br>
     * <b>title : </b> 更新数据集 <br>
     * <b>description :</b>更新数据集 <br>
     */
    public void update(List<T> data) {
        clear();
        if (data != null) {
            mDataCache.addAll(data);
        }
    }

    /**
     * <b>description :</b> 清除缓存数据 <br>
     */
    public void clear() {
        mDataCache.clear();
    }

    /**
     * <b>description :</b>添加数据集，向数据缓存中添加多个元素。 <br>
     */
    public void add(List<T> data) {
        if (data != null) {
            mDataCache.addAll(data);
        }
    }

    /**
     * <b>description :</b>添加数据元素，向数据缓存中添加单个元素。 <br>
     */
    public void add(T data) {
        if (data != null) {
            mDataCache.add(data);
        }
    }

    /**
     * <b>description :</b> 交换两个元素的位置 <br>
     */
    public void exchange(int src, int target) {
        T endObject = getItem(target);
        T startObject = getItem(src);
        mDataCache.set(src, endObject);
        mDataCache.set(target, startObject);
    }

    public void remove(int position) {
        int pos = getRealPosition(position);
        mDataCache.remove(pos);
    }

    @Override
    public int getCount() {
        return mDataCache.size();
    }

    @Override
    public T getItem(int position) {
        int pos = getRealPosition(position);
        return mDataCache.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 返回真实的position
     */
    protected int getRealPosition(int position) {
        int size = mDataCache.size();
        if (size == 0) {
            return 0;
        }

        int pos = position % size;
        return pos;
    }
}
