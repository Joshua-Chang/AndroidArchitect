package org.devio.hi.ui.refresh;

public enum  HiOverViewState {
    /**
     * 初始态
     */
    STATE_INIT,
    /**
     * Header展示的状态
     */
    STATE_VISIBLE,
    /**
     * 超出可刷新距离的状态(头部全部拉出)
     */
    STATE_OVER,
    /**
     * 刷新中的状态
     */
    STATE_REFRESH,
    /**
     * 超出刷新位置松开手后的状态
     */
    STATE_OVER_RELEASE
}