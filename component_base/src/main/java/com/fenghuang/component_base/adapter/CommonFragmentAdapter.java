package com.fenghuang.component_base.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fenghuang.component_base.base.LazyLoadFragment;

import java.util.List;

/**
 * Create by wangchao on 2018/7/18 10:13
 */
public class CommonFragmentAdapter extends FragmentPagerAdapter {
    private List<LazyLoadFragment> mFragments;

    public CommonFragmentAdapter(FragmentManager fm, List<LazyLoadFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
