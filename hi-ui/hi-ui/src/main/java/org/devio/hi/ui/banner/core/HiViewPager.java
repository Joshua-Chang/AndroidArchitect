package org.devio.hi.ui.banner.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class HiViewPager extends ViewPager {
    private int mIntervalTime;
    private boolean mAutoPlay = true;//自动轮播
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        public void run() {
            next();
            mHandler.postDelayed(this, mIntervalTime);//延时一定时间执行下一次
        }
    };
    public HiViewPager(@NonNull Context context) {
        super(context);
    }
    public void setIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
    }
    public void setAutoPlay(boolean mAutoPlay) {
        this.mAutoPlay = mAutoPlay;
        if (mAutoPlay) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
    private int next(){
        int nextPosition=-1;
        if (getAdapter()==null||getAdapter().getCount()<=1) {
            pause();
            return nextPosition;
        }
        nextPosition=getCurrentItem()/*index*/+1;
        if (nextPosition>=getAdapter().getCount()) {
            // TODO: 2020/12/10 越界后轮播
            nextPosition = ((HiBannerAdapter) getAdapter()).getFirstItem();
        }
        /*真正移动，Set the currently selected page 与上文set index */
        setCurrentItem(nextPosition,true);/*True to smoothly scroll to the new item, false to transition immediately*/
        return nextPosition;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                start();break;
            default:
                pause();
        }
        return super.onTouchEvent(ev);
    }

    private void start() {
        mHandler.removeCallbacksAndMessages(null);
        if (mAutoPlay) {
            mHandler.postDelayed(mRunnable,mIntervalTime);
        }
    }

    private void pause() {
        mHandler.removeCallbacksAndMessages(null);
    }



    /*--------------------------------------------------扩展----------------------------------------------------------------------*/

    private boolean isLayout;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isLayout = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isLayout && getAdapter() != null && getAdapter().getCount() > 0) {
            try {
                //fix 使用RecyclerView + ViewPager bug https://blog.csdn.net/u011002668/article/details/72884893
                Field mScroller = ViewPager.class.getDeclaredField("mFirstLayout");
                mScroller.setAccessible(true);
                mScroller.set(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        //fix 使用RecyclerView + ViewPager bug
        if (((Activity) getContext()).isFinishing()) {
            super.onDetachedFromWindow();
        }
        pause();
    }


    /**
     * 切换时长
     * @param duration
     */
    public void  setScrollDuration(int duration){
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new HiBannerScroller(getContext(), duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
