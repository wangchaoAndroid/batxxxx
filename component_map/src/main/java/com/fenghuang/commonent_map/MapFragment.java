package com.fenghuang.commonent_map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/7/18 13:56
 */
public class MapFragment extends LazyLoadFragment{

    private MapView mMapView;
    private AMap mAMap;
    public MyLocationStyle myLocationStyle;
    Polyline polyline;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    public GeoFenceClient mGeoFenceClient;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient ;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;
    //定义接收广播的action字符串
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
    MapNetServices mMapNetServices = RetrofitManager.getInstance().initRetrofit().create(MapNetServices.class);
    /**
     * 是否开启围栏
     */
    private boolean isOpen;
    //声明定位回调监听器
    public AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {

                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    //绘制面
    public void circle(double v1, double v2,int radius) {
        LatLng latLng = new LatLng(v1, v2);

        mAMap.addCircle(new CircleOptions().center(latLng)
                .radius(radius).strokeColor(R.color.basic_blue3)
                .fillColor(R.color.basic_blue3).strokeWidth(2));
        Log.e("tag", "============圈圈300==================2");
    }

    public void initLocation(Context context){
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        //option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
    }

    public void initMap(){
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                RxToast.info(latLng.longitude + "----" + latLng.latitude);
            }
        });
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.strokeColor(android.R.color.transparent);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.showMyLocation(true);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mUiSettings = mAMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(true);

        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    /**
     * 初始化围栏组件
     */
    public void init( FenchModel fenchModel){
        //围栏初始化
        //注册监听事件
        mGeoFenceClient = new GeoFenceClient(getActivity());
        mGeoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN| GeoFenceClient.GEOFENCE_OUT| GeoFenceClient.GEOFENCE_STAYED);
        //设置回调监听
        mGeoFenceClient.setGeoFenceListener(fenceListenter);
        //创建并设置PendingIntent
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        //可在其中解析amapLocation获取相应内容。
        DPoint dPoint = new DPoint(fenchModel.latitude,fenchModel.longitude);
        circle(fenchModel.latitude,fenchModel.longitude,fenchModel.meter);
        addFence(dPoint,fenchModel.meter,"您已进入围栏范围");

    }



    public void setAMapLocationListener(){
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
    }

    //开始定位
    public void startLocation(){
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    //停止定位
    public void stopLocation(){
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
    }



    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.map);
        mAMap = mMapView.getMap();
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        regeist();
    }

    public void addFence(DPoint point, float radius, String customId){
        mGeoFenceClient.addGeoFence(point,radius,customId);
    }

    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {

                //获取Bundle
                Bundle bundle = intent.getExtras();
                //获取围栏行为：
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                //获取自定义的围栏标识：
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                //获取围栏ID:
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                //获取当前有触发的围栏对象：
                GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);
                RxToast.warning(customId +"");
            }
        }
    };

    //注册围栏广播
    public void regeist(){
        IntentFilter filter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        getActivity().registerReceiver(mGeoFenceReceiver, filter);
    }

    //取消注册广播
    public void unRegeist(){
        if(mGeoFenceReceiver != null){
            getActivity().unregisterReceiver(mGeoFenceReceiver);
        }
    }

    //清除所有围栏
    public void clearFence(){
        mGeoFenceClient.removeGeoFence();
    }

    public void destoryClient(){
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    //创建回调监听
    GeoFenceListener fenceListenter = new GeoFenceListener() {
        @Override
        public void onGeoFenceCreateFinished(List<GeoFence> geoFenceList, int errorCode, String s) {
            if(errorCode == GeoFence.ADDGEOFENCE_SUCCESS){//判断围栏是否创建成功
                //tvReult.setText();
                Log.e(TAG,"添加围栏成功!!" + errorCode);
                //geoFenceList就是已经添加的围栏列表，可据此查看创建的围栏
            } else {
                //geoFenceList就是已经添加的围栏列表
                //.setText("添加围栏失败!!");
                Log.e(TAG,"添加围栏失败!!" +  errorCode );
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //clearFence();
        unRegeist();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_map;
    }

    @Override
    protected void lazyLoad() {
        initMap();
        initLocation(getActivity());
        setAMapLocationListener();
        startLocation();
        addOnClickListeners(R.id.start_fench);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if(id == R.id.start_fench){
            if(isOpen){
                swtichFench(0,1);
            }else {
                swtichFench(1,1);
            }
        }
    }

    private void swtichFench(int status,int type) {
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        ILog.e(TAG,token);
        mMapNetServices.switchEnclosure(token,status,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<FenchModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<FenchModel> value) {
                        //后台返回围栏信息
                        if(value != null){
                            FenchModel fenchModel = value.obj;
                            ILog.e(TAG,"" + fenchModel.toString());
                            init(fenchModel);
                        }

                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
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

    /**
     * 获取围栏信息
     */
    public void getFenchInfo(){

    }

}
