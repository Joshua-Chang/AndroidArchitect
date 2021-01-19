package org.devio.hi.ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

public interface IHiTab<INFO> extends IHiTabLayout.OnTabSelectedListener<INFO> {
    void setHiTabInfo(@NonNull INFO data);

    /**
     * 动态修改某个item的大小
     *
     * @param height
     */
    void resetHeight(@Px int height);

}