package cm.android.common.ui.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Gallery;
import android.widget.ViewFlipper;
import cm.android.util.MyLog;

public class MyAutoFlipView extends Gallery {

	public MyAutoFlipView(Context context) {
		super(context);
		init();
	}

	public MyAutoFlipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyAutoFlipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setCallbackDuringFling(false);
		setAutoStart(true);
	}

	private static final int DEFAULT_INTERVAL = 3000;

	private int mFlipInterval = DEFAULT_INTERVAL;
	private boolean mAutoStart = false;

	private boolean mRunning = false;
	private boolean mStarted = false;
	private boolean mVisible = false;
	private boolean mUserPresent = true;

	// setFilpInterval：设置View切换的时间间隔。参数为毫秒。
	// startFlipping：开始进行View的切换，时间间隔是上述方法设置的间隔数。切换会循环进行。
	// stopFlipping：停止View切换。
	// setAutoStart：设置是否自动开始。如果设置为“true”，当ViewFlipper显示的时候View的切换会自动开始。

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				mUserPresent = false;
				updateRunning();
			} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
				mUserPresent = true;
				updateRunning(false, 0);// 立即开始
			}
		}
	};

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		// Listen for broadcasts related to user-presence
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		getContext().registerReceiver(mReceiver, filter);

		if (mAutoStart) {
			// Automatically start when requested
			startFlipping();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mVisible = false;

		getContext().unregisterReceiver(mReceiver);
		updateRunning();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mVisible = (visibility == VISIBLE);
		updateRunning(false, mFlipInterval);
	}

	/**
	 * How long to wait before flipping to the next view
	 * 
	 * @param milliseconds
	 *            time in milliseconds
	 */
	public void setFlipInterval(int milliseconds) {
		mFlipInterval = milliseconds;
	}

	/**
	 * Start a timer to cycle through child views
	 */
	public void startFlipping() {
		mStarted = true;
		updateRunning();
	}

	/**
	 * No more flips
	 */
	public void stopFlipping() {
		mStarted = false;
		updateRunning();
	}

	@Override
	public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
		super.onInitializeAccessibilityEvent(event);
		event.setClassName(ViewFlipper.class.getName());
	}

	@Override
	public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
		super.onInitializeAccessibilityNodeInfo(info);
		info.setClassName(ViewFlipper.class.getName());
	}

	/**
	 * Internal method to start or stop dispatching flip {@link Message} based
	 * on {@link #mRunning} and {@link #mVisible} state.
	 */
	private void updateRunning() {
		updateRunning(true, mFlipInterval);
	}

	/**
	 * Internal method to start or stop dispatching flip {@link Message} based
	 * on {@link #mRunning} and {@link #mVisible} state.
	 * 
	 * @param flipNow
	 *            Determines whether or not to execute the animation now, in
	 *            addition to queuing future flips. If omitted, defaults to
	 *            true.
	 */
	private void updateRunning(boolean flipNow, int flipInterval) {
		boolean running = mVisible && mStarted && mUserPresent;
		if (running != mRunning) {
			if (running) {
				// showOnly(mWhichChild, flipNow);
				Message msg = mHandler.obtainMessage(FLIP_MSG);
				mHandler.sendMessageDelayed(msg, flipInterval);
			} else {
				mHandler.removeMessages(FLIP_MSG);
			}
			mRunning = running;
		}
		if (MyLog.isDebug()) {
			MyLog.d("updateRunning() mVisible=" + mVisible + ", mStarted="
					+ mStarted + ", mUserPresent=" + mUserPresent
					+ ", mRunning=" + mRunning);
		}
	}

	/**
	 * Returns true if the child views are flipping.
	 */
	public boolean isFlipping() {
		return mStarted;
	}

	/**
	 * Set if this view automatically calls {@link #startFlipping()} when it
	 * becomes attached to a window.
	 */
	public void setAutoStart(boolean autoStart) {
		mAutoStart = autoStart;
	}

	/**
	 * Returns true if this view automatically calls {@link #startFlipping()}
	 * when it becomes attached to a window.
	 */
	public boolean isAutoStart() {
		return mAutoStart;
	}

	private final int FLIP_MSG = 1;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == FLIP_MSG) {
				if (mRunning) {
					if (mAutoStart) {
						onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
					}
					msg = obtainMessage(FLIP_MSG);
					sendMessageDelayed(msg, mFlipInterval);
				}
			}
		}
	};

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		updateFlip(hasWindowFocus);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			// Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			// Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean start = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			start = false;
		}
		// TODO 有时候会返回MotionEvent.ACTION_CANCEL
		// else if (event.getAction() == MotionEvent.ACTION_UP) {
		// start = true;
		// }
		else {
			start = true;
		}
		updateFlip(start);
		return super.onTouchEvent(event);
	}

	private void updateFlip(boolean start) {
		if (start) {
			if (mAutoStart) {
				startFlipping();
			}
		} else {
			stopFlipping();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}
}
