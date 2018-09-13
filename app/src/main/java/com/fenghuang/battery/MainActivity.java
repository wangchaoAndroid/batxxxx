package com.fenghuang.battery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.fenghuang.battery.helper.BottomNavigationViewHelper;
import com.fenghuang.battery.helper.FragmentFactory;
import com.fenghuang.component_base.adapter.CommonFragmentAdapter;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.utils.ContextManager;
import com.fenghuang.component_base.utils.ViewFinder;
import com.fenghuang.component_base.view.ViewPagerStop;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";
    private BottomNavigationView mBottomNavigationView;
    private ViewPagerStop mViewPager;

    @Override
    protected void setEvent() {

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_tab1:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.item_tab2:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.item_tab3:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.item_tab4:
                        mViewPager.setCurrentItem(3);
                        break;
                }
                return true;

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        ViewFinder viewFinder = new ViewFinder(this);
        mBottomNavigationView = (BottomNavigationView)viewFinder.find(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mViewPager = viewFinder.find(R.id.vp_view);
        mBottomNavigationView.setItemIconTintList(null);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        List<LazyLoadFragment> fragments = new ArrayList<>();
        fragments.add(FragmentFactory.createFragment(0,null));
        fragments.add(FragmentFactory.createFragment(1,null));
        fragments.add(FragmentFactory.createFragment(2,null));
        fragments.add(FragmentFactory.createFragment(3,null));
        mViewPager.setAdapter(new CommonFragmentAdapter(getSupportFragmentManager(),fragments));

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(position).getItemId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 11 && resultCode == RESULT_OK){
            mViewPager.setCurrentItem(1);
        }
    }
}
