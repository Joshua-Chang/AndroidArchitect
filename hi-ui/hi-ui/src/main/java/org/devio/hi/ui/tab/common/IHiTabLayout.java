package org.devio.hi.ui.tab.common;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface IHiTabLayout<Tab extends ViewGroup, INFO> {
    Tab findTab(@NonNull INFO data);

    void addTabSelectedChangeListener(OnTabSelectedListener<INFO> listener);

    void defaultSelected(@NonNull INFO defaultInfo);

    void inflateInfo(@NonNull List<INFO> infoList);

    interface OnTabSelectedListener<INFO> {
        void onTabSelectedChange(int index, @Nullable INFO lastSelectedInfo, @NonNull INFO selectedInfo);
    }
}
