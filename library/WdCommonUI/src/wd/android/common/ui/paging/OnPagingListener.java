package wd.android.common.ui.paging;

import java.util.EventListener;

public abstract interface OnPagingListener extends EventListener {
	public abstract void onNextPage(PageEvent parampageEvent);

	public abstract void onPrevPage(PageEvent parampageEvent);

	public abstract void onFirstPage(PageEvent parampageEvent);

	public abstract void onLastPage(PageEvent parampageEvent);

	public abstract void onGotoPage(PageEvent parampageEvent, int paramInt);
}
