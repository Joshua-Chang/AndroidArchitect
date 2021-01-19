package org.devio.hi.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.devio.hi.library.util.HiDisplayUtil;


public abstract class HiOverView extends FrameLayout {

    protected HiOverViewState mState = HiOverViewState.STATE_INIT;
    public int mPullRefreshHeight;//触发下拉刷新需要的高度
    public float minDamp=1.6f;
    public float maxDamp=2.2f;

    public abstract void init();
    //通知头部
    protected abstract void onScroll(int scrollY,int pullRefreshHeight);
    public abstract void onVisible();
    public abstract void onOver();//下拉超过头部，释放就会加载
    public abstract void onRefresh();
    public abstract void onFinish();
    public void setState(HiOverViewState state){
        this.mState=state;
    }

    public HiOverViewState getState() {
        return mState;
    }

    public HiOverView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        preInit();
    }

    public HiOverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public HiOverView(Context context) {
        super(context);
        preInit();
    }

    protected void preInit() {
        mPullRefreshHeight = HiDisplayUtil.dp2px(66, getResources());
        init();
    }
}
