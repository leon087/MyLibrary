package cm.android.common.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import cm.android.util.ObjectUtil;

import java.util.List;

/**
 * @desc : 抽象Adapter类
 * @param <T>
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {

	/** 数据缓存 */
	protected List<T> mDataCache = ObjectUtil.newArrayList();

	/** 用于从XML文件中创建Layout */
	protected LayoutInflater mInflater;
	protected Context context;

	/**
	 * </br><b>description : </b> 创建Adapter，需要给定View创建接口。
	 * 
	 * @param inflater
	 * @param creator
	 */
	public AbstractAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	/**
	 * <br>
	 * <b>title : </b> 更新数据集 <br>
	 * <b>description :</b>更新数据集 <br>
	 * 
	 * @param data
	 */
	public void update(List<T> data) {
		clear();
		if (data != null) {
			mDataCache.addAll(data);
		}
		notifyDataSetChanged();
	}

	/**
	 * <b>description :</b> 清除缓存数据 <br>
	 */
	public void clear() {
		mDataCache.clear();
		notifyDataSetChanged();
	}

	/**
	 * <b>description :</b>添加数据集，向数据缓存中添加多个元素。 <br>
	 * 
	 * @param set
	 */
	public void add(List<T> set) {
		if (set != null) {
			mDataCache.addAll(set);
			notifyDataSetChanged();
		}
	}

	/**
	 * <b>description :</b>添加数据元素，向数据缓存中添加单个元素。 <br>
	 * 
	 * @param item
	 */
	public void add(T item) {
		if (item != null) {
			mDataCache.add(item);
			notifyDataSetChanged();
		}
	}

	/**
	 * <b>description :</b> 交换两个元素的位置 <br>
	 * 
	 * @param src
	 * @param target
	 */
	public void exchange(int src, int target) {
		T endObject = getItem(target);
		T startObject = getItem(src);
		mDataCache.set(src, endObject);
		mDataCache.set(target, startObject);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		int pos = getRealPosition(position);
		mDataCache.remove(pos);
		notifyDataSetChanged();
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
	 * 
	 * @param position
	 * @return
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
