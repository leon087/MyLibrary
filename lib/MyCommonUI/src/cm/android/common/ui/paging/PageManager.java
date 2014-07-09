package cm.android.common.ui.paging;

import java.util.HashSet;
import java.util.Iterator;

public class PageManager {
	private static PageManager pm = new PageManager();

	// 总页数
	private int pageCount = 0;
	// 每一页个数
	private int pageSize = 10;
	// 总个数
	private int itemCount = 0;
	// 当前页数
	private int currentPage = 1;
	// 开始索引
	private int startIndex = 0;
	// 结束索引
	private int lastIndex = 0;

	protected HashSet<OnPagingListener> listeners = new HashSet();
	private PageEvent pEvent = null;

	private PageManager() {
	}

	public static PageManager getInstance() {
		if (pm == null) {
			pm = new PageManager();
		}
		return pm;
	}

	public void addPagingListener(OnPagingListener listener) {
		if (this.listeners == null) {
			this.listeners = new HashSet();
		}
		this.listeners.add(listener);
	}

	public void removePagedListtener(OnPagingListener listener) {
		if (this.listeners == null) {
			return;
		}
		this.listeners.remove(listener);

		this.pEvent = new PageEvent(this);
		notifyListeners(this.pEvent);
	}

	public void notifyListeners(PageEvent e) {
		Iterator iter = this.listeners.iterator();
		while (iter.hasNext()) {
			OnPagingListener listener = (OnPagingListener) iter.next();
			e.onPageEvent(e);
			switch (e.getEventCode()) {
			case 4:
				listener.onFirstPage(e);
				break;
			case 2:
				listener.onPrevPage(e);
				break;
			case 3:
				listener.onNextPage(e);
				break;
			case 1:
				listener.onLastPage(e);
				break;
			case 5:
				listener.onGotoPage(e, e.getEventPage());
			}
		}
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		measurePage();
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
		measurePage();
	}

	public int getPageCount() {
		return this.pageCount;
	}

	public int getCurrentPage() {
		return this.currentPage;
	}

	public int getStartIndex() {
		return this.startIndex;
	}

	public int getLastIndex() {
		return this.lastIndex;
	}

	public void nextPage() {
		if (this.currentPage <= this.pageCount)
			this.currentPage += 1;
		else {
			this.currentPage = 1;
		}
		measurePage();
		if (this.listeners == null) {
			return;
		}
		this.pEvent = new PageEvent(this);
		this.pEvent.onNextPage(this.pEvent, this.currentPage);
		notifyListeners(this.pEvent);
	}

	public void prevPage() {
		if (this.currentPage > 0)
			this.currentPage -= 1;
		else {
			this.currentPage = 1;
		}
		measurePage();
		if (this.listeners == null) {
			return;
		}
		this.pEvent = new PageEvent(this);
		this.pEvent.onPrevPage(this.pEvent, this.currentPage);
		notifyListeners(this.pEvent);
	}

	public void lastPage() {
		this.currentPage = this.pageCount;
		measurePage();
		if (this.listeners == null) {
			return;
		}
		this.pEvent = new PageEvent(this);
		this.pEvent.onLastPage(this.pEvent, this.currentPage);
		notifyListeners(this.pEvent);
	}

	public void firstPage() {
		this.currentPage = 1;
		measurePage();
		if (this.listeners == null) {
			return;
		}
		this.pEvent = new PageEvent(this);
		this.pEvent.onFirstPage(this.pEvent, this.currentPage);
		notifyListeners(this.pEvent);
	}

	private void measurePage() {
		if (this.itemCount < this.pageSize) {
			this.pageCount = 1;
		} else if (this.itemCount % this.pageSize == 0)
			this.pageCount = (this.itemCount / this.pageSize);
		else {
			this.pageCount = (this.itemCount / this.pageSize + 1);
		}

		this.startIndex = ((this.currentPage - 1) * this.pageSize);
		this.lastIndex = this.currentPage * this.pageSize;
		if (this.currentPage > this.pageCount) {
			firstPage();
		}
	}
}
