package cm.android.common.ui.paging;

import java.util.EventObject;

public class PageEvent extends EventObject {

    public static final int EVENT_PAGE_LAST = 1;

    public static final int EVENT_PAGE_PREV = 2;

    public static final int EVENT_PAGE_NEXT = 3;

    public static final int EVENT_PAGE_FIRST = 4;

    public static final int EVENT_PAGE_GOTO = 5;

    private static final long serialVersionUID = 1L;

    private int code;

    private int page;

    public PageEvent(Object source) {
        super(source);
    }

    public int getEventCode() {
        return this.code;
    }

    public int getEventPage() {
        return this.page;
    }

    public void onPageEvent(PageEvent e) {
        switch (e.getEventCode()) {
            case EVENT_PAGE_NEXT:
                onNextPage(e, getEventPage());
                break;
            case EVENT_PAGE_PREV:
                onPrevPage(e, getEventPage());
                break;
            case EVENT_PAGE_LAST:
                onLastPage(e, getEventPage());
                break;
            case EVENT_PAGE_FIRST:
                onFirstPage(e, getEventPage());
                break;
            case EVENT_PAGE_GOTO:
                onGotoPage(e, getEventPage());
        }
    }

    public void onFirstPage(PageEvent e, int page) {
        this.page = page;
        this.code = 4;
    }

    public void onPrevPage(PageEvent e, int page) {
        this.page = page;
        this.code = 2;
    }

    public void onNextPage(PageEvent e, int page) {
        this.page = page;
        this.code = 3;
    }

    public void onLastPage(PageEvent e, int page) {
        this.page = page;
        this.code = 1;
    }

    public void onGotoPage(PageEvent e, int page) {
        this.page = page;
        this.code = 5;
    }

}
