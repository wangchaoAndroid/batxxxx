package com.fenghuang.component_battery;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView count_down_time;


    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mImageView = view.findViewById(R.id.app_enter_map);
        mBannerView = view.findViewById(R.id.app_banner);
        count_down_time = view.findViewById(R.id.count_down_time);
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
        SpannableString spannableString = new SpannableString("19小时30分");
        //设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍   ,0.5表示一半
        spannableString.setSpan(new RelativeSizeSpan(2f), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN),0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(2f), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN),4,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        count_down_time.setText(spannableString);
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
