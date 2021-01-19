package org.devio.hi.ui.banner.core;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * HiViewPager的适配器，为页面填充数据
 */
public class HiBannerAdapter extends PagerAdapter {
    // TODO: 2020/12/10 instantiateItem destroyItem getItemPosition 的关系
    //  FragmentPagerAdapter和PagerAdapter
    private Context mContext;
    private IHiBanner.OnBannerClickListener mBannerClickListener;
    private List<? extends HiBannerMo> models;
    private boolean mAutoPlay = true;
    private boolean mLoop = false;//循环切换
    private int mLayoutResId = -1;

    public HiBannerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnBannerClickListener(IHiBanner.OnBannerClickListener OnBannerClickListener) {
        this.mBannerClickListener = OnBannerClickListener;
    }

    public void setLayoutResId(@LayoutRes int layoutResId) {
        this.mLayoutResId = layoutResId;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
    }

    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }


    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        this.models = models;
        initCachedView();
        notifyDataSetChanged();
    }

    //页面数量
    public int getRealCount() {
        return models == null ? 0 : models.size();
    }

    private void initCachedView() {
        mCachedViews = new SparseArray<>();
        for (int i = 0; i < models.size(); i++) {
            HiBannerViewHolder viewHolder = new HiBannerViewHolder();
            mCachedViews.put(i, viewHolder);
        }
    }

    private IBindAdapter mBindAdapter;
    private SparseArray<HiBannerViewHolder> mCachedViews = new SparseArray<>();//缓存每个pager

    public void setBindAdapter(IBindAdapter bindAdapter) {
        this.mBindAdapter = bindAdapter;
    }

    @Override
    public int getCount() {
        return mAutoPlay ? Integer.MAX_VALUE : (mLoop ? Integer.MAX_VALUE : getRealCount());
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public class HiBannerViewHolder {
        private SparseArray<View> viewHolderSparseArr;//子view缓存
        View rootView;

        HiBannerViewHolder() {
            if (mLayoutResId == -1) {
                throw new IllegalArgumentException("you must be set setLayoutResId first");
            }
            this.rootView = LayoutInflater.from(mContext).inflate(mLayoutResId, null, false);
        }

        public View getRootView() {
            return rootView;
        }

        public <V extends View> V findViewById(int id) {//根据id在rootview内查找view
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;//没有子view直接返回
            }
            if (this.viewHolderSparseArr == null) {
                this.viewHolderSparseArr = new SparseArray<>(1);
            }
            V childView = (V) viewHolderSparseArr.get(id);//先在缓存找
            if (childView == null) {
                childView = rootView.findViewById(id);//在rootview找
                this.viewHolderSparseArr.put(id, childView);//放入缓存
            }
            return childView;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //让item每次都会刷新
        return POSITION_NONE;
    }

    /**
     * 获取初次展示的item位置
     *
     * @return
     */
    public int getFirstItem() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
        //初始化 将这个值设为首个index，越界时取这个值去恢复
        //理论上其他巨大的值应该都行
        //(Integer.MAX_VALUE / 2) % getRealCount() ：取余得到小于realcount的数
        //最终结果：Integer.MAX_VALUE / 2减去小于realcount的数，更靠近中心
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (getRealCount() > 0)
            position %= getRealCount();
        HiBannerViewHolder viewHolder = mCachedViews.get(position);
        if (container.equals(viewHolder.rootView.getParent())) {
            container.removeView(viewHolder.rootView);
        }
        int finalPosition = position;
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBannerClickListener != null) {
                    mBannerClickListener.onBannerClick(viewHolder, models.get(finalPosition), finalPosition);
                }
            }
        });
        if (mBindAdapter != null) {
            mBindAdapter.onBind(viewHolder, models.get(finalPosition), position);
        }
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }
}
