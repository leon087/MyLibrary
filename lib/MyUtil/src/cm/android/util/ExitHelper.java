package cm.android.util;

import android.view.KeyEvent;

/**
 * 程序退出控制管理
 */
public class ExitHelper {

	/**
	 * 退出操作接口
	 */
	public static interface IExitInterface {
		/**
		 * 显示退出提示
		 */
		void showExitTip();

		/**
		 * 退出
		 */
		void exit();
	}

	/**
	 * 按两次返回键退出
	 * 
	 */
	public static class TwicePressHolder {
		private long lastTime = 0;
		private IExitInterface iExitInterface;

		private int delay = 1000;

		/**
		 * 按两次返回键退出
		 * 
		 * @param iExitInterface
		 * @param delay
		 */
		public TwicePressHolder(IExitInterface iExitInterface, int delay) {
			this.iExitInterface = iExitInterface;
			this.delay = delay;
		}

		/**
		 * 在onKeyDown()中调用，返回false则说明未处理
		 * 
		 * @param keyCode
		 * @param event
		 * @return
		 */
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				if ((System.currentTimeMillis() - lastTime) > delay) {
					iExitInterface.showExitTip();
					lastTime = System.currentTimeMillis();
				} else {
					iExitInterface.exit();
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * 长按返回键退出
	 */
	public static class LongPressHolder {
		private IExitInterface iExitInterface;

		public LongPressHolder(IExitInterface iExitInterface) {
			this.iExitInterface = iExitInterface;
		}

		/**
		 * 在dispatchKeyEvent()中调用，返回false则说明未处理
		 * 
		 * @param event
		 * @return
		 */
		public boolean dispatchKeyEvent(KeyEvent event) {
			int keyCode = event.getKeyCode();
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (event.isLongPress()) {
					iExitInterface.exit();
					return true;
				} else {
					iExitInterface.showExitTip();
					return true;
				}
			}
			return false;
		}
	}

}
