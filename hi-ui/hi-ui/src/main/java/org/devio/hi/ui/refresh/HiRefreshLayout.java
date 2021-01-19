package org.devio.hi.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 下拉-》超过触发高度-》松手-》恢复到触发高度-》刷新-》外部完成调用-》(从触发高度)恢复到原始高度
 * gesture          event      recover                          recover
 * 下拉-》未达到触发高度-》松手-》恢复到原始高度
 */
public class HiRefreshLayout extends FrameLayout implements HiRefresh {
    private boolean disableRefreshScroll;//刷新时是否禁止滚动
    private GestureDetector mGestureDetector;//手势探测器
    private OnRefreshListener mOnRefreshListener;//回调给外部刷新用
    protected HiOverView mHiOverView;//刷新头  //回调给自定义的头布局用
    private int mLastY;//下拉距离（下拉Y坐标）

    /**
     * 应该用View.computeScroll和View.indivate这两个api，来实现滚动，
     * 不应该用handle.post，因为前者具有vsync特性，16ms毫秒执行一次，
     * 后者直接使用handle.post可能会导致16ms内，滚动了好几次，这是没有意义的，浪费cpu的做法。
     */
    private AutoScroller mAutoScroller; //线程滚动器

    public HiRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HiRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public HiRefreshLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), hiGestureDetectorListener);
        mAutoScroller = new AutoScroller();
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void setRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    @Override
    public void setRefreshOverView(HiOverView hiOverView) {
        if (this.mHiOverView != null) {
            removeView(mHiOverView);
        }
        this.mHiOverView = hiOverView;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mHiOverView, 0, params);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //定义head和child的排列位置
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            if (mHiOverView.getState() == HiOverViewState.STATE_REFRESH) {//刷新中：展开状态
                // TODO: 2020/12/8 top换为0试验一下
                /*head.getMeasuredHeight()不变，不同类型header的高度可能不一样。
                 * 假如触发高度65，header高35：在刷新时Top为30
                 * 假如触发高度65，header高85：在刷新时Top为-20，即下拉到65触发（也可以继续再拉到85），刷新发生时最终回归65*/
                head.layout(0, mHiOverView.mPullRefreshHeight - head.getMeasuredHeight(), right, mHiOverView.mPullRefreshHeight);
                child.layout(0, mHiOverView.mPullRefreshHeight, right, child.getMeasuredHeight() + mHiOverView.mPullRefreshHeight);
            } else {
                int childTop = child.getTop();
                /*未下拉：childTop=0时，header完全隐藏*/
                /*下拉： childTop>0时，childTop-head.getMeasuredHeight */
                /*假如触发高度65，header高35，childTop34时，header：top=-1，bottom=34*/
                /*假如触发高度65，header高35，childTop65时，header：top=20，bottom=65*/
                /*假如触发高度65，header高85，childTop65时，header：top=-20，bottom=65*/
                head.layout(0, childTop - head.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, child.getMeasuredHeight() + childTop);
            }
            View other;
            for (int i = 2; i < getChildCount(); i++) {
                other = getChildAt(i);
                other.layout(left, right, top, bottom);
            }
        }
    }


    HiGestureDetectorListener hiGestureDetectorListener = new HiGestureDetectorListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float disX, float disY) {
            //横向、禁用不处理
            if (Math.abs(disX) > Math.abs(disY) ||
                    mOnRefreshListener != null && !mOnRefreshListener.enableRefresh())
                return false;// TODO: 2020/12/8 && 是否需要优化
            //刷新时禁止滑动 消费掉
            if (disableRefreshScroll && mHiOverView.getState() == HiOverViewState.STATE_REFRESH)
            {
                return true;
            }
            //若触发了内部的滚动控件，则不触发下拉刷新
            View head = getChildAt(0);
            View child = HiScrollUtil.findScrollableChild(HiRefreshLayout.this);
            if (HiScrollUtil.childScrolled(child)) {
                //如果列表发生了滚动则不处理
                return false;
            }
            // step1、列表逐渐下拉
            //TODO 下拉不管是否超过触发高度，只有松手释放后，才能触发刷新
            if ((head.getBottom() > 0 || disY <= 0.0f/*下滑为负数*/) &&
                    (mHiOverView.getState() != HiOverViewState.STATE_REFRESH ||
                            head.getBottom() <= mHiOverView.mPullRefreshHeight)// TODO: 2020/12/8 小于还是小于等于
            ) {
                //尚在滑动中，并未释放
                if (mHiOverView.getState() != HiOverViewState.STATE_OVER_RELEASE) {
                    int compressDisY;
                    /*下拉的运动距离：本来应该是和disY同步，除上阻尼后，让下拉到刷新距离变慢，下拉到越界距离更慢*/
                    if (child.getTop() < mHiOverView.mPullRefreshHeight) {
                        compressDisY = (int) (mLastY / mHiOverView.minDamp);
                    } else {
                        compressDisY = (int) (mLastY / mHiOverView.maxDamp);
                    }
                    // step2、头部逐渐下移
                    boolean bool = moveDown(compressDisY, false);//处理下拉运动 compressDisY正数
                    mLastY = (int) (-disY);//下滑为负数，要取正数
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 位移处理
     * 在Gesture将header拉出时是手动下移      下拉时
     * 在Scroller将露出的header收回是自动上移  松手时  未达到高度回滚也是
     * @param offsetY 偏移量
     * @param isAuto  自动滚动触发
     * @return
     */
    private boolean moveDown(int offsetY, boolean isAuto) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        // TODO: 2020/12/9 试验一下这个判断
        /*方法在手势事件中一直被调用，getTop和offset也一直改变，是增量*/
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {// step9、回归原始状态
            offsetY = -child.getTop();
            //移动head与child的位置，到原始位置
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mHiOverView.getState() != HiOverViewState.STATE_REFRESH) {
                mHiOverView.setState(HiOverViewState.STATE_INIT);
            }
        } else if (childTop <= mHiOverView.mPullRefreshHeight) {//还没超出设定的刷新距离
            // step3、头部露出
            if (mHiOverView.getState() != HiOverViewState.STATE_VISIBLE && !isAuto) {
                //开始显示头部,并触发回调
                mHiOverView.onVisible();
                mHiOverView.setState(HiOverViewState.STATE_VISIBLE);
            }
            //开始滚动
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            //当拉出高度>=触发高度释放时，比如85，child.getTop()+offsetY(负数),一点点直到=65时，触发刷新
            //todo 当切仅当：拉出高度>=触发高度,然后释放时 才触发刷新
            if (childTop == mHiOverView.mPullRefreshHeight && mHiOverView.getState() == HiOverViewState.STATE_OVER_RELEASE) {
                // step7、一点点回滚到释放高度时 开始刷新  85=>65
                refresh();
            }
        } else if (mHiOverView.getState() == HiOverViewState.STATE_REFRESH &&
                childTop > mHiOverView.mPullRefreshHeight
        ) {//正在刷新中，禁止继续下拉
            return false;
        } else {
            // step4、头部超过触发高度  85>65
            if (mHiOverView.getState() != HiOverViewState.STATE_OVER && !isAuto) {
                //超出刷新位置
                mHiOverView.onOver();
                mHiOverView.setState(HiOverViewState.STATE_OVER);
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mHiOverView != null) {
            mHiOverView.onScroll(head.getBottom(), mHiOverView.mPullRefreshHeight);
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //事件分发处理
        if (!mAutoScroller.isFinished()) {
            return false;
        }
        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP ||/*松手状态*/
                ev.getAction() == MotionEvent.ACTION_CANCEL ||
                ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            if (head.getBottom() > 0) {
                if (mHiOverView.getState() != HiOverViewState.STATE_REFRESH) {//松手恢复
                    // step5、松手超出部分回滚到触发高度  85->65      （未达到触发高度时松手恢复）
                    recover(head.getBottom());
                    return false;
                }
            }
            mLastY = 0;//恢复完成
            // TODO: 2020/12/8 if (head.getBottom() > 0) 是否要和上边的判断聚合，及mLastY
        }

        /*除了松手外的其他事件,真正的下拉处理*/
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        if ((consumed || (mHiOverView.getState() != HiOverViewState.STATE_INIT && mHiOverView.getState() != HiOverViewState.STATE_REFRESH))
                && head.getBottom() != 0
        ) {
            ev.setAction(MotionEvent.ACTION_CANCEL);//让父布局接受不到真实的事件
            return super.dispatchTouchEvent(ev);
        }
        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }


    /**
     * 复原到初始状态：1、MotionEvent松手 2、刷新完成
     * 走线程去自动的moveDown
     * @param dy  恢复指定的长度，而非恢复到某长度
     */
    private void recover(int dy) {
        //dis >高度 ：STATE_OVER_RELEASE
        if (dy > mHiOverView.mPullRefreshHeight&&mOnRefreshListener != null) {//超出触发高度的复原
            mAutoScroller.recover(dy-mHiOverView.mPullRefreshHeight);//恢复指定的长度，而非恢复到某长度
            mHiOverView.setState(HiOverViewState.STATE_OVER_RELEASE);
        } else {//未达到、刷新完的复原
            mAutoScroller.recover(dy);// step8、1
        }
    }
    private class AutoScroller implements Runnable {
        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        public AutoScroller() {
            mScroller=new Scroller(getContext(),new LinearInterpolator());
            mIsFinished=true;
        }
        boolean isFinished(){return mIsFinished;}

        void recover(int dy){
            if (dy<=0) {
                return;
            }
            removeCallbacks(this);
            mLastY=0;
            mIsFinished=false;
            // step6、自动回滚
            mScroller.startScroll(0,0,0,dy,300);
            post(this);
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {//true还在compute 未完成、false完成
                // step6、自动回滚 85 -1 -1 -1 ... 65        // step8、2刷新完的回滚65 -1 -1 -1 ...0
                moveDown(mLastY-mScroller.getCurrY(),true);
                mLastY=mScroller.getCurrY();
                //mLastY只提供将 moveDown线性的作用 让moveDown(-1,true)自动执行，直到scroll完成
                //此处的offset都是负数，作用是将露出的header收回
//                mLastY  0  0  1  2
//                mCurrY  0  1  2  3
//                offset  0 -1 -1 -1

                post(this);
            }else {
                removeCallbacks(this);
                mIsFinished=true;
            }
        }
    }


    private void refresh() {
        if (mOnRefreshListener != null) {
            mHiOverView.setState(HiOverViewState.STATE_REFRESH);
            mHiOverView.onRefresh();//回调给自定义的头布局用
            mOnRefreshListener.onRefresh();//回调给外部刷新用
        }
    }
    /**
     * 外部（UI接收到数据）刷新完成时，调用来隐藏
     */
    @Override
    public void refreshFinished() {
        View head = getChildAt(0);
        mHiOverView.onFinish();
        mHiOverView.setState(HiOverViewState.STATE_INIT);
        int bottom = head.getBottom();
        if (bottom > 0) {//底部到父布局顶部的距离
            // step8、外部设置刷新结束后，回滚到原始状态 65->0
            recover(bottom);
        }
    }
}
