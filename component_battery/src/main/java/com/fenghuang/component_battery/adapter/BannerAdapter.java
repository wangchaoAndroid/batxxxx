package com.fenghuang.component_battery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fenghuang.component_base.base.BaseApp;
import com.fenghuang.component_base.tool.ImageLoader;
import com.fenghuang.component_battery.R;
import com.fenghuang.component_battery.bean.Ad;

import java.util.List;

/**
 * Create by wangchao on 2018/7/23 09:38
 */
public class BannerAdapter extends PagerAdapter {
    private Context mContext;
    private  List<Ad> ads;

    public BannerAdapter(Context context, List<Ad> adList) {
        mContext = context;
        this.ads = adList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView view = new ImageView(mContext);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        ImageLoader.load(ads.get(position % ads.size() ).advertisingjpg,view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
