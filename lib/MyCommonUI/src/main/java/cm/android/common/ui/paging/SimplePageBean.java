package cm.android.common.ui.paging;

/**
 * 简单分页对象
 */
public class SimplePageBean {

    /**
     * 索引从1开始
     */
    public static final int FIRST_PAGE_INDEX = 1;

    /**
     * 当前页数(从1开始)
     */
    private int currentPage = FIRST_PAGE_INDEX;

    /**
     * 每一页个数
     */
    private int pageItemCount = 10;

    public SimplePageBean(int pageItemCount) {
        super();
        this.currentPage = FIRST_PAGE_INDEX;
        this.pageItemCount = pageItemCount;
    }

    /**
     * 跳转到指定页数
     */
    public int toPage(int pageIndex) {
        currentPage = pageIndex;
        return currentPage;
    }

    /**
     * 重置当前页到首页
     */
    public int reset() {
        return toPage(FIRST_PAGE_INDEX);
    }

    /**
     * 下一页
     */
    public int nextPage() {
        currentPage++;
        return currentPage;
    }

    /**
     * 上一页
     */
    public int prevPage() {
        if (currentPage > FIRST_PAGE_INDEX) {
            currentPage--;
        }
        return currentPage;
    }

    /**
     * 获取当前页
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 获取每页个数
     */
    public int getPageItemCount() {
        return pageItemCount;
    }
}
