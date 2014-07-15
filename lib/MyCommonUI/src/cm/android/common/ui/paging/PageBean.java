package cm.android.common.ui.paging;

/**
 * 分页对象
 */
public class PageBean {
    // 每一页个数
    private int pageItemCount = 10;
    // 总个数
    private int totalItemCount = 0;
    // 总页数
    private int totalPage = 0;
    // 当前页数
    private int currentPage = 1;
    // 当前页开始索引，从0开始
    private int startIndex = 0;
    // 当前页结束索引
    private int lastIndex = 0;

    // 是否支持循环翻页
    private boolean isRepeat = true;

    /**
     * 创建一个分页管理对象
     *
     * @param totalItemCount 总个数
     * @param pageItemCount  每一页个数
     * @param isRepeat       是否支持循环翻页
     */
    public PageBean(int totalItemCount, int pageItemCount, boolean isRepeat) {
        init(totalItemCount, pageItemCount, isRepeat);
    }

    private void init(int totalItemCount, int pageItemCount, boolean isRepeat) {
        this.totalItemCount = totalItemCount;
        this.pageItemCount = pageItemCount;
        this.isRepeat = isRepeat;
        // 刷新相关数据
        currentPage = 1;
        totalPage = (this.totalItemCount - 1) / this.pageItemCount + 1;
        measureData();
    }

    /**
     * 根据总个数刷新数据
     *
     * @param totalItemCount
     * @return
     */
    public int reset(int totalItemCount) {
        if (totalItemCount == this.totalItemCount) {
            return currentPage;
        }
        this.totalItemCount = totalItemCount;
        totalPage = (this.totalItemCount - 1) / this.pageItemCount + 1;
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }
        measureData();
        return currentPage;
    }

    /**
     * 根据总个数和单页显示的个数刷新数据
     *
     * @param totalItemCount
     * @return
     */
    public int reset(int totalItemCount, int pageItemCount) {
        // if (totalItemCount == this.totalItemCount) {
        // return currentPage;
        // }
        this.totalItemCount = totalItemCount;
        this.pageItemCount = pageItemCount;
        totalPage = (this.totalItemCount - 1) / this.pageItemCount + 1;
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }
        measureData();
        return currentPage;
    }

    // TODO
    public int initCurrentPage(int index) {
        int currentPage = index / this.pageItemCount + 1;
        if (this.currentPage != currentPage) {
            this.currentPage = currentPage;
        }
        measureData();
        return this.currentPage;
    }

    /**
     * 下一页
     *
     * @return
     */
    public int nextPage() {
        if (currentPage < totalPage) {
            currentPage++;
        } else {
            if (!isRepeat) {
                return currentPage;
            }
            currentPage = 1;
        }
        measureData();
        return currentPage;
    }

    /**
     * 上一页
     *
     * @return
     */
    public int prevPage() {
        if (currentPage > 1) {
            currentPage--;
        } else {
            if (!isRepeat) {
                return currentPage;
            }
            currentPage = totalPage;
        }
        measureData();
        return currentPage;
    }

    /**
     * 返回当前页
     *
     * @return
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 获取当前页起始索引
     *
     * @return
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * 获取当前页最后一条数据索引
     *
     * @return
     */
    public int getLastIndex() {
        return lastIndex;
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 获取每页个数
     *
     * @return
     */
    public int getPageItemCount() {
        return pageItemCount;
    }

    private void measureData() {
        this.startIndex = getPageStartIndex(currentPage);
        this.lastIndex = getPageLastIndex(currentPage);
    }

    /**
     * 获取指定页数最后一条数据索引
     *
     * @param page
     * @return
     */
    public int getPageLastIndex(int page) {
        if (page < 1 || page > totalPage) {
            return -1;
        }

        if (page == totalPage) {
            return this.totalItemCount - 1;
        } else {
            return page * this.pageItemCount - 1;
        }
    }

    /**
     * 获取指定页数起始索引
     *
     * @param page
     * @return
     */
    public int getPageStartIndex(int page) {
        if (page < 1 || page > totalPage) {
            return -1;
        }

        return ((page - 1) * this.pageItemCount);
    }

    /**
     * 第一页时不再循环翻页
     *
     * @param myPageBean
     * @return
     */
    public boolean isFirstPage() {
        if (!isRepeat && getCurrentPage() == 1) {
            return true;
        }
        return false;
    }

    /**
     * 最后一页时不再循环翻页
     *
     * @param myPageBean
     * @return
     */
    public boolean isLastPage() {
        if (!isRepeat && getCurrentPage() == getTotalPage()) {
            return true;
        }
        return false;
    }
}
