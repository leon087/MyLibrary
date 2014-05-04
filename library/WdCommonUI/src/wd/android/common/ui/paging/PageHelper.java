package wd.android.common.ui.paging;

import java.util.ArrayList;
import java.util.List;

import wd.android.util.util.MyLog;

public class PageHelper {
	private List<Object> totalItems = new ArrayList<Object>();
	private int eachRequestCount = 48;
	private int requestStartIndex = 0;

	public PageHelper(List<Object> totalItems, int requestStartIndex,
			int eachRequestCount) {
		if (null != totalItems) {
			this.totalItems = totalItems;
		}
		this.requestStartIndex = requestStartIndex;
		this.eachRequestCount = eachRequestCount;
	}

	public List<Object> getCurrentPage(PageBean pageBean) {
		int requestStart = getRequestStartIndex(pageBean);
		if (requestStart != requestStartIndex) {
			return null;
		}
		int startIndex = pageBean.getStartIndex();
		int endIndex = pageBean.getLastIndex();
		int start = startIndex % eachRequestCount;
		int end = endIndex % eachRequestCount + 1;

		if (end > totalItems.size()) {
			MyLog.e("end = " + end);
			end = totalItems.size();
		}
		if (start > end) {
			MyLog.e("start = " + start);
			start = 0;
		}
		return totalItems.subList(start, end);
	}

	public List<Object> getPageData(PageBean pageBean, int page) {
		if (page < 0 || page > pageBean.getTotalPage()) {
			return new ArrayList<Object>();
		}
		int startIndex = pageBean.getPageStartIndex(page);
		int endIndex = pageBean.getPageLastIndex(page);
		int start = startIndex % eachRequestCount;
		int end = endIndex % eachRequestCount + 1;

		if (end > totalItems.size()) {
			MyLog.e("end = " + end);
			end = totalItems.size();
		}
		if (start > end) {
			MyLog.e("start = " + start);
			start = 0;
		}
		return totalItems.subList(start, end);

	}

	public int getRequestStartIndex(PageBean pageBean) {
		int startIndex = pageBean.getStartIndex();
		int requestStartIndex = (startIndex / eachRequestCount * eachRequestCount);
		return requestStartIndex;

	}
}
