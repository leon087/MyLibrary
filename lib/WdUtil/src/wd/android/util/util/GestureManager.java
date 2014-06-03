package wd.android.util.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 手势管理类
 */
public class GestureManager extends SimpleOnGestureListener implements
		OnTouchListener {

	private GestureDetector mGestureDetector;
	private OnGestureFlingListener mListener;

	public GestureManager(Context context, OnGestureFlingListener listener) {
		mGestureDetector = new GestureDetector(context, this);
		mListener = listener;
	}

	/**
	 * 注册touch监听
	 * 
	 * @param view
	 */
	public void register(View view) {
		view.setLongClickable(true);
		view.setOnTouchListener(this);
	}

	/**
	 * 去注册touch监听
	 * 
	 * @param view
	 */
	public void unRegister(View view) {
		view.setOnTouchListener(null);
	}

	// @Override
	// public boolean onDown(MotionEvent arg0) {
	// return false;
	// }

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 50) {
			return mListener.onFling(OnGestureFlingListener.TYPE_LEFT);
		} else if (e1.getX() - e2.getX() < -50) {
			return mListener.onFling(OnGestureFlingListener.TYPE_RIGHT);
		} else if (e1.getY() - e2.getY() > 50) {
			return mListener.onFling(OnGestureFlingListener.TYPE_UP);
		} else if (e1.getY() - e2.getY() < -50) {
			return mListener.onFling(OnGestureFlingListener.TYPE_DOWN);
		}
		return false;
	}

	// @Override
	// public void onLongPress(MotionEvent e) {
	//
	// }
	//
	// @Override
	// public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
	// float distanceY) {
	// return false;
	// }
	//
	// @Override
	// public void onShowPress(MotionEvent e) {
	//
	// }
	//
	// @Override
	// public boolean onSingleTapUp(MotionEvent e) {
	// return false;
	// }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	/**
	 * 手势动作接口
	 */
	public interface OnGestureFlingListener {
		/**
		 * 向左滑动
		 */
		public static final int TYPE_LEFT = 0x01;
		/**
		 * 向右滑动
		 */
		public static final int TYPE_RIGHT = TYPE_LEFT + 1;
		/**
		 * 向上滑动
		 */
		public static final int TYPE_UP = TYPE_LEFT + 2;
		/**
		 * 向下滑动
		 */
		public static final int TYPE_DOWN = TYPE_LEFT + 3;

		/**
		 * 手势滑动时出发
		 * 
		 * @param type
		 *            手势滑动类型
		 */
		boolean onFling(int type);
	}

}
