package org.devio.hi.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import org.devio.hi.ui.R;
import org.devio.hi.ui.banner.core.HiBannerAdapter;
import org.devio.hi.ui.banner.core.HiViewPager;
import org.devio.hi.ui.banner.core.IBindAdapter;
import org.devio.hi.ui.banner.core.HiBannerMo;
import org.devio.hi.ui.banner.core.IHiBanner;
import org.devio.hi.ui.banner.indicator.HiCircleIndicator;
import org.devio.hi.ui.banner.indicator.HiIndicator;

import java.util.List;

/**
 * 核心问题：
 * 1. 如何实现UI的高度定制？
 * 2. 作为有限的item如何实现无线轮播呢？
 * 3. Banner需要展示网络图片，如何将网络图片库和Banner组件进行解耦？
 * 4. 指示器样式各异，如何实现指示器的高度定制？
 * 5. 如何设置ViewPager的滚动速度？
 */
public class HiBanner extends FrameLayout implements IHiBanner, ViewPager.OnPageChangeListener {

    public HiBanner(@NonNull Context context) {
        this(context, null);
    }

    public HiBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HiBanner);
        boolean autoPlay = typedArray.getBoolean(R.styleable.HiBanner_autoPlay, true);
        boolean loop = typedArray.getBoolean(R.styleable.HiBanner_loop, false);
        int intervalTime = typedArray.getInteger(R.styleable.HiBanner_intervalTime, -1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(intervalTime);
        typedArray.recycle();
    }

    
    private HiBannerAdapter mAdapter;
    private HiIndicator mHiIndicator;
    private boolean mAutoPlay;
    private boolean mLoop;
    private List<? extends HiBannerMo> mHiBannerMos;
    /*目前没用到。主要目的扩展用：如果外部自己相对OnPageChangeListener做操作，可以回调出去*/
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mIntervalTime = 5000;
    private HiBanner.OnBannerClickListener mOnBannerClickListener;
    private HiViewPager mHiViewPager;
    private int mScrollDuration = -1;
    public void setAdapter(HiBannerAdapter adapter) {
        this.mAdapter = adapter;
    }
    @Override
    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }
    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            this.mIntervalTime = intervalTime;
        }
    }
    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }
    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.mOnBannerClickListener = onBannerClickListener;
    }






    @Override
    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        setBannerData(R.layout.hi_banner_item_image, models);
    }

    @Override
    public void setHiIndicator(HiIndicator hiIndicator) {
        this.mHiIndicator = hiIndicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (mAdapter != null) mAdapter.setAutoPlay(autoPlay);
        if (mHiViewPager != null) mHiViewPager.setAutoPlay(autoPlay);
    }
    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }
    @Override
    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
        if (mHiViewPager != null && duration > 0) mHiViewPager.setScrollDuration(duration);
    }
    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends HiBannerMo> models) {
        mHiBannerMos = models;
        init(layoutResId);
    }
    private void init(@LayoutRes int layoutResId) {
        if (mAdapter == null) {
            mAdapter = new HiBannerAdapter(getContext());
        }
        if (mHiIndicator == null) {
            mHiIndicator = new HiCircleIndicator(getContext());
        }

        //indicator
        mHiIndicator.onInflate(mHiBannerMos.size());

        //adapter
        mAdapter.setLayoutResId(layoutResId);
        mAdapter.setBannerData(mHiBannerMos);
        mAdapter.setAutoPlay(mAutoPlay);
        mAdapter.setLoop(mLoop);
        mAdapter.setOnBannerClickListener(mOnBannerClickListener);
        //pager
        mHiViewPager = new HiViewPager(getContext());
        mHiViewPager.setIntervalTime(mIntervalTime);
        mHiViewPager.addOnPageChangeListener(this);
        mHiViewPager.setAutoPlay(mAutoPlay);
        if (mScrollDuration > 0) mHiViewPager.setScrollDuration(mScrollDuration);
        mHiViewPager.setAdapter(mAdapter);

        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        if ((mLoop || mAutoPlay) && mAdapter.getRealCount() != 0) {
            //无限轮播关键点：使第一张能反向滑动到最后一张，已达到无限滚动的效果
            int firstItem = mAdapter.getFirstItem();
            //setCurrentItem:移动到index位置pager
            mHiViewPager.setCurrentItem(firstItem, false);
        }

        //清除缓存view
        this.removeAllViews();
        this.addView(mHiViewPager, layoutParams);
        this.addView(mHiIndicator.get(), layoutParams);
    }





    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != mOnPageChangeListener && mAdapter.getRealCount() != 0) {
            mOnPageChangeListener.onPageScrolled(position % mAdapter.getRealCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter.getRealCount() == 0) {
            return;
        }
        position = position % mAdapter.getRealCount();
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mHiIndicator != null) {
            mHiIndicator.onPointChange(position, mAdapter.getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}


