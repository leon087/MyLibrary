package cm.android.common.ui.view;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterViewFlipper;

@TargetApi(12)
public class MyViewFlipper extends AdapterViewFlipper {
	private static final int ANIMATOR_DURATION = 300;

	public MyViewFlipper(Context context) {
		super(context);
	}

	public MyViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private static class AnimatorHolder {
		private ObjectAnimator in;
		private ObjectAnimator out;

		public static AnimatorHolder next(View target) {
			AnimatorHolder animatorHolder = new AnimatorHolder();

			animatorHolder.in = ObjectAnimator.ofFloat(target,
					View.TRANSLATION_X, target.getWidth(), 0f);
			animatorHolder.in.setDuration(ANIMATOR_DURATION);

			animatorHolder.out = ObjectAnimator.ofFloat(target,
					View.TRANSLATION_X, 0, -target.getWidth());
			animatorHolder.out.setDuration(ANIMATOR_DURATION);

			return animatorHolder;
		}

		public static AnimatorHolder prev(View target) {
			AnimatorHolder animatorHolder = new AnimatorHolder();

			animatorHolder.in = ObjectAnimator.ofFloat(target,
					View.TRANSLATION_X, -target.getWidth(), 0f);
			animatorHolder.in.setDuration(ANIMATOR_DURATION);

			animatorHolder.out = ObjectAnimator.ofFloat(target,
					View.TRANSLATION_X, 0, target.getWidth());
			animatorHolder.out.setDuration(ANIMATOR_DURATION);

			return animatorHolder;
		}
	}

	@Override
	public void showNext() {
		AnimatorHolder animatorHolder = AnimatorHolder.next(this);
		setInAnimation(animatorHolder.in);
		setOutAnimation(animatorHolder.out);

		super.showNext();
	}

	@Override
	public void showPrevious() {
		AnimatorHolder animatorHolder = AnimatorHolder.prev(this);
		setInAnimation(animatorHolder.in);
		setOutAnimation(animatorHolder.out);

		super.showPrevious();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		updateFlip(hasWindowFocus);
	}

	private void updateFlip(boolean start) {
		if (start) {
			if (isAutoStart()) {
				startFlipping();
			}
		} else {
			stopFlipping();
		}
	}

}
