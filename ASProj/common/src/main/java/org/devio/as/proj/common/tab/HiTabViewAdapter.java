package org.devio.as.proj.common.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.devio.hi.ui.tab.bottom.HiTabBottomInfo;

import java.util.List;

public class HiTabViewAdapter {
    private List<HiTabBottomInfo<?>> mInfoList;
    private Fragment mCurFragment;
    private FragmentManager mFragmentManager;

    public HiTabViewAdapter(FragmentManager mFragmentManager,List<HiTabBottomInfo<?>> infoList) {
        this.mInfoList = infoList;
        this.mFragmentManager = mFragmentManager;
    }

    public void instantiateItem(View container, int position) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mCurFragment != null) {
            transaction.hide(mCurFragment);
        }
        String name = container.getId() + ":" + position;
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            transaction.show(fragment);
        }else {
            fragment=getItem(position);
            if (!fragment.isAdded()) {
                transaction.add(container.getId(),fragment,name);
            }
        }
        mCurFragment=fragment;
        transaction.commitAllowingStateLoss();
    }

    public Fragment getCurrentFragment() {
        return mCurFragment;
    }

    public Fragment getItem(int position){
        try {
            return (Fragment) mInfoList.get(position).fragment.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getCount(){
        return mInfoList.isEmpty()?0:mInfoList.size();
    }
}
