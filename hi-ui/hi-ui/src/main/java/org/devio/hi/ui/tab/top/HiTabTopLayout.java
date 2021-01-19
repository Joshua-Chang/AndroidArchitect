package org.devio.hi.ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import org.devio.hi.library.util.HiDisplayUtil;
import org.devio.hi.ui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HiTabTopLayout extends HorizontalScrollView implements IHiTabLayout<HiTabTop, HiTabTopInfo<?>> {
    private List<OnTabSelectedListener<HiTabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private HiTabTopInfo<?> lastSelectedInfo;
    private List<HiTabTopInfo<?>> infoList;

    public HiTabTopLayout(Context context) {
        this(context, null);
    }

    public HiTabTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }


    @Override
    public void inflateInfo(@NonNull List<HiTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) return;
        this.infoList = infoList;
        LinearLayout linearLayout = getRootLayout(true);
        lastSelectedInfo = null;
        Iterator<OnTabSelectedListener<HiTabTopInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabTop) {
                iterator.remove();
            }
        }
        for (int i = 0; i < infoList.size(); i++) {
            HiTabTopInfo<?> tabTopInfo = infoList.get(i);
            HiTabTop tabTop = new HiTabTop(getContext());
            tabTop.setHiTabInfo(tabTopInfo);
            tabSelectedChangeListeners.add(tabTop);
            linearLayout.addView(tabTop);
            tabTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(tabTopInfo);
                }
            });
        }
    }

    private void onSelected(HiTabTopInfo<?> selectedInfo) {
        for (OnTabSelectedListener<HiTabTopInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(selectedInfo/*当前序列*/), this.lastSelectedInfo, selectedInfo);
        }
        this.lastSelectedInfo = selectedInfo;
        autoScroll(selectedInfo);
    }


    @Override
    public HiTabTop findTab(@NonNull HiTabTopInfo<?> info) {
        ViewGroup ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof HiTabTop) {
                HiTabTop tab = (HiTabTop) child;
                if (tab.getHiTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabTopInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(rootView, params);
        } else if (clear) {
            rootView.removeAllViews();
        }
        return rootView;
    }

    int tabWith;

    private void autoScroll(HiTabTopInfo<?> selectedInfo) {
        HiTabTop tabTop = findTab(selectedInfo);
        if (tabTop == null) return;
        int index = infoList.indexOf(selectedInfo);
        int[] loc = new int[2];
        tabTop.getLocationInWindow(loc);
        int scrollWidth;
        if (tabWith == 0) {
            tabWith = tabTop.getWidth();
        }
        if ((loc[0] + tabWith / 2)/*选中tab的中心*/ > HiDisplayUtil.getDisplayWidthInPx(getContext())/2) {
            scrollWidth = rangeScrollWidth(index, 2);//右
        } else {
            scrollWidth = rangeScrollWidth(index, -2);
        }
        scrollTo(getScrollX() + scrollWidth, 0);
    }

    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        //一个tab一个的滚动，最大range个
        for (int i = 0; i < Math.abs(range); i++) {
            int next;
            if (range < 0) {//从距当前tab的最远处，往近处算：远处越界可直接返回0
                next=index+range+i;//此时range为负数
            } else {
                next=index+range-i;
            }
            // TODO: 2021/1/15 从右边往左点，点到正序第三个时
            if (next>=0/*=0为从右边往左点，点到正序第三个时*/&&next<infoList.size()) {
                if (range<0){
                    scrollWidth -= scrollWidth(next, false);
                }else {
                    scrollWidth += scrollWidth(next, true);
                }
            }
        }
        return scrollWidth;
    }

    private int scrollWidth(int index, boolean toRight) {
        HiTabTop target = findTab(infoList.get(index));
        if (target == null) return 0;
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (toRight) {
            if (rect.right>tabWith) {//从距当前tab的最远处，往近处算，可能是第二个
                return tabWith;
            }else {
                return tabWith-rect.right;
            }
        }else {
            if (rect.left<=-tabWith) {
                return tabWith;
            }else if (rect.left > 0) {//显示部分
                return rect.left;
            }
        }
        return 0;
    }
}
