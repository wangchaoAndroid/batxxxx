package com.fenghuang.component_battery;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
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
    private MapView mMapView;
    private ViewPager mBannerView;
    private TextView count_down_time;
    List<Ad> items = new ArrayList<>();
    private BannerAdapter mBannerAdapter;
    private TextView lastPrecent;

    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mMapView = view.findViewById(R.id.app_enter_map);
        mBannerView = view.findViewById(R.id.app_banner);
        count_down_time = view.findViewById(R.id.count_down_time);
        lastPrecent = view.findViewById(R.id.last_precent);
        mAMap = mMapView.getMap();
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        addOnClickListeners(R.id.app_enter_map,R.id.left2);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_battery;
    }

    @Override
    protected void lazyLoad() {

        initMap();
        mBannerAdapter = new BannerAdapter(getActivity(),items);
        mBannerView.setAdapter(mBannerAdapter);
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
                            int temperature = homeModel.electricquantity;
                            lastPrecent.setText(temperature + "%");
                            List<Ad> advertisingtabList = homeModel.advertisingtabList;
                            items.clear();
                            items.addAll(advertisingtabList);
                            mBannerAdapter.notifyDataSetChanged();
                            LatLng latLng = new LatLng(homeModel.latitude,homeModel.longitude);
                            MarkerOptions markerOption = new MarkerOptions();
                            markerOption.position(latLng);
//                            markerOption.snippet("上次电池终端定位点");
                            markerOption.draggable(false);//设置Marker可拖动
                            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                    .decodeResource(getResources(),R.drawable.circle_blue)));
                            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                            markerOption.setFlat(true);//设置marker平贴地图效果
                            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                            mAMap.addMarker(markerOption);
                        }


                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e);
                    }
                });
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
    private AMap mAMap;
    public MyLocationStyle myLocationStyle;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    public void initMap(){
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                CC.obtainBuilder("component_map")
                        .setActionName("enter_router_map")
                        .build()
                        .call();
            }
        });
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.strokeColor(android.R.color.transparent);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.showMyLocation(false);

//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mUiSettings = mAMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(false);
        mUiSettings.setAllGesturesEnabled(false);
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setScaleControlsEnabled(false);
        mUiSettings.setZoomControlsEnabled(false);
        mAMap.setMyLocationEnabled(false);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

}
