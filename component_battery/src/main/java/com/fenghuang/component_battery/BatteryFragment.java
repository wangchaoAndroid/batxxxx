package com.fenghuang.component_battery;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fenghuang.component_base.base.BaseApp;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.helper.GlideRoundTransform;
import com.fenghuang.component_base.tool.ImageLoader;
import com.fenghuang.component_battery.adapter.BannerAdapter;
import com.fenghuang.component_battery.adapter.BatteryAdapter;
import com.fenghuang.component_base.helper.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/18 10:26
 */
public class BatteryFragment  extends LazyLoadFragment{

    private ImageView mImageView;
    private ViewPager mBannerView;


    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mImageView = view.findViewById(R.id.app_enter_map);
        mBannerView = view.findViewById(R.id.app_banner);
        addOnClickListeners(R.id.app_enter_map);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_battery;
    }

    @Override
    protected void lazyLoad() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            items.add(i + "");
        }
        BannerAdapter bannerAdapter = new BannerAdapter(getActivity(),items);
        mBannerView.setAdapter(bannerAdapter);
        ImageLoader.load(R.drawable.default1,mImageView);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int  viewId = view.getId();
        if(viewId == R.id.app_enter_map){
            CC.obtainBuilder("component_map")
                    .setActionName("enter_router_map")
                    .build()
                    .call();
        }
    }
}
