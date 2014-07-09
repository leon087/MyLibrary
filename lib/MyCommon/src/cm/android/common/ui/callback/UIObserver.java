package cm.android.common.ui.callback;

public interface UIObserver {
	public void register(UICallback uiCallback);

	public void unRegister(UICallback uiCallback);
}
