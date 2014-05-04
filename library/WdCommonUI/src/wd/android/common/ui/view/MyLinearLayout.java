package wd.android.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	private FocusFinder mFocusFinder = FocusFinder.getInstance();

	private OnSlidingAtEndListener onSlidingAtEndListener;
	private int keyType = OnSlidingAtEndListener.TYPE_KEY_UP
			| OnSlidingAtEndListener.TYPE_KEY_DOWN
			| OnSlidingAtEndListener.TYPE_KEY_LEFT
			| OnSlidingAtEndListener.TYPE_KEY_RIGHT;

	public MyLinearLayout(Context context) {
		super(context);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setKeyListenType(int type) {
		keyType = type;
	}

	private boolean dispatchKeyLeft() {
		return dispatchKey(OnSlidingAtEndListener.TYPE_KEY_LEFT,
				View.FOCUS_LEFT);
	}

	private boolean dispatchKeyRight() {
		return dispatchKey(OnSlidingAtEndListener.TYPE_KEY_RIGHT,
				View.FOCUS_RIGHT);
	}

	private boolean dispatchKeyUp() {
		return dispatchKey(OnSlidingAtEndListener.TYPE_KEY_UP, View.FOCUS_UP);
	}

	private boolean dispatchKeyDown() {
		return dispatchKey(OnSlidingAtEndListener.TYPE_KEY_DOWN,
				View.FOCUS_DOWN);
	}

	private boolean dispatchKey(int constantKeyType, int viewFocusType) {
		if ((keyType & constantKeyType) == constantKeyType) {
			View nextFocus = mFocusFinder.findNextFocus(this,
					getFocusedChild(), viewFocusType);

			if (nextFocus == null) {
				if (onSlidingAtEndListener != null) {
					boolean result = onSlidingAtEndListener.onSlidingAtEnd(
							constantKeyType, this, getFocusedChild());
					if (result) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		int keyAction = event.getAction();
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				&& keyAction == KeyEvent.ACTION_DOWN) {
			if (dispatchKeyLeft()) {
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				&& keyAction == KeyEvent.ACTION_DOWN) {
			if (dispatchKeyRight()) {
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP
				&& keyAction == KeyEvent.ACTION_DOWN) {
			if (dispatchKeyUp()) {
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
				&& keyAction == KeyEvent.ACTION_DOWN) {
			if (dispatchKeyDown()) {
				return true;
			}
		}

		return super.dispatchKeyEvent(event);
	}

	public void selectLeft(View lastFocus, View nextFocus, boolean animation) {
		if (lastFocus == null || nextFocus == null)
			return;
		// nextFocus.requestFocus();
	}

	public void selectRight(View lastFocus, View nextFocus, boolean animation) {
		if (lastFocus == null || nextFocus == null)
			return;
		// nextFocus.requestFocus();
	}

	public void selectUp(View lastFocus, View nextFocus, boolean animation) {
		if (lastFocus == null || nextFocus == null)
			return;
		// nextFocus.requestFocus();
	}

	public void selectDown(View lastFocus, View nextFocus, boolean animation) {
		if (lastFocus == null || nextFocus == null)
			return;
		// nextFocus.requestFocus();
	}

	public void setOnSlidingAtEndListener(OnSlidingAtEndListener l) {
		this.onSlidingAtEndListener = l;
	}

	public interface OnSlidingAtEndListener {
		public static final int TYPE_KEY_UP = 0x0001;
		public static final int TYPE_KEY_DOWN = 0x0010;
		public static final int TYPE_KEY_LEFT = 0x0100;
		public static final int TYPE_KEY_RIGHT = 0x1000;

		boolean onSlidingAtEnd(int type, ViewGroup viewGroup, View view);
	}
}
