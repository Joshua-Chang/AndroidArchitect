package org.devio.hi.ui.refresh;

public interface HiRefresh {
//    /**
//     * 是否禁用刷新
//     * @param disableRefresh
//     */
//    void setDisableRefresh(boolean disableRefresh);
    /**
     * 刷新时是否禁止滚动
     * @param disableRefreshScroll
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 刷新完成
     */
    void refreshFinished();

    /**
     * 设置下拉刷新的监听器
     *
     * @param onRefreshListener 刷新的监听器
     */
    void setRefreshListener(OnRefreshListener onRefreshListener);

    /**
     * 设置下拉刷新的视图
     *
     * @param hiOverView 下拉刷新的视图
     */
    void setRefreshOverView(HiOverView hiOverView);

    interface OnRefreshListener {
        void onRefresh();
        boolean enableRefresh();
    }
}
