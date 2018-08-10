package com.fenghuang.battery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fenghuang.battery.R;
import com.fenghuang.battery.fragment.SpFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/8/10 13:42
 */
public class SpalashAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public SpalashAdapter(FragmentManager fm) {
        super(fm);
    }

    public SpalashAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        mFragments = fragmentList;
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
