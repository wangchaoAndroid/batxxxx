package com.fenghuang.component_battery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.ImageLoader;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_battery.adapter.BannerAdapter;
import com.fenghuang.component_battery.bean.Ad;
import com.fenghuang.component_battery.bean.HomeModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/7/18 10:26
 */
public class BatteryFragment  extends LazyLoadFragment{
    private static final String TAG = "BatteryFragment";
    private ImageView mImageView;
    private ViewPager mBannerView;
    private TextView count_down_time;
    List<Ad> items = new ArrayList<>();
    private BannerAdapter mBannerAdapter;

    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mImageView = view.findViewById(R.id.app_enter_map);
        mBannerView = view.findViewById(R.id.app_banner);
        count_down_time = view.findViewById(R.id.count_down_time);
        addOnClickListeners(R.id.app_enter_map,R.id.left2);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_battery;
    }

    @Override
    protected void lazyLoad() {


        mBannerAdapter = new BannerAdapter(getActivity(),items);
        mBannerView.setAdapter(mBannerAdapter);
        ImageLoader.load(R.drawable.default1,mImageView);
        SpannableString spannableString = new SpannableString("最新告警内容");
        //设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍   ,0.5表示一半
//        spannableString.setSpan(new RelativeSizeSpan(2f), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN),0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new RelativeSizeSpan(2f), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN),4,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        count_down_time.setText(spannableString);
        getHomeInfo();
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
        }else if(viewId == R.id.left2){
            startActivity(new Intent(getActivity(),WarnActivity.class));
        }
    }

    public void getHomeInfo(){
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        ILog.e(TAG,token);
        BatteryNetServices batteryNetServices = RetrofitManager.getInstance().initRetrofit().create(BatteryNetServices.class);
        batteryNetServices.getHome(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<HomeModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<HomeModel> value) {
                        ILog.e(TAG,value + "");
                        if(value != null){
                            items.clear();
                            HomeModel homeModel = value.obj;
                            List<Ad> advertisingtabList = homeModel.advertisingtabList;
                            items.clear();
                            items.addAll(advertisingtabList);
                            mBannerAdapter.notifyDataSetChanged();
                        }


                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e);
                    }
                });
    }
}
