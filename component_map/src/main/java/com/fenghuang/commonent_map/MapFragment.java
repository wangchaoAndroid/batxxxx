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

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/18 13:56
 */
public class MapFragment extends LazyLoadFragment implements AMap.OnMyLocationChangeListener {

    private List<LatLng> latLngs = new ArrayList<LatLng>();
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


    //声明定位回调监听器
    public AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    DPoint dPoint = new DPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
                    circle(amapLocation.getLatitude(), amapLocation.getLongitude());
                    addFence(dPoint,500f,"111111");
                    stopLocation();
                    mLocationClient.unRegisterLocationListener(this);
                    destoryClient();
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
    public void circle(double v1, double v2) {
        LatLng latLng = new LatLng(v1, v2);

        mAMap.addCircle(new CircleOptions().center(latLng)
                .radius(300).strokeColor(Color.BLUE)
                .fillColor(Color.BLUE).strokeWidth(2));
        Log.e("tag", "============圈圈300==================2");
    }

    /**
     * 初始化围栏组件
     * @param context
     */
    public void init(Context context){
        //围栏初始化
        //注册监听事件
        mGeoFenceClient = new GeoFenceClient(getActivity());
        mGeoFenceClient.setActivateAction(GeoFenceClient.GEOFENCE_IN| GeoFenceClient.GEOFENCE_OUT| GeoFenceClient.GEOFENCE_STAYED);
        //设置回调监听
        mGeoFenceClient.setGeoFenceListener(fenceListenter);
        //创建并设置PendingIntent
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
         //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        //option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
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

                Log.e("1111","99999999999999999999");

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
        getActivity().unregisterReceiver(mGeoFenceReceiver);
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
                Log.e("1111","添加围栏成功!!" + errorCode);
                //geoFenceList就是已经添加的围栏列表，可据此查看创建的围栏
            } else {
                //geoFenceList就是已经添加的围栏列表
                //.setText("添加围栏失败!!");
                Log.e("1111","添加围栏失败!!" +  errorCode );
            }
        }
    };

    @Override
    public void onMyLocationChange(Location location) {
        latLngs.add(new LatLng(location.getLatitude(),location.getLongitude()));
        if(polyline != null){
            polyline.remove();
        }
        polyline =mAMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
    }

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
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.showMyLocation(true);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mUiSettings = mAMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(true);
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setOnMyLocationChangeListener(this);
        latLngs.clear();
        regeist();
        init(getActivity());
        setAMapLocationListener();
        startLocation();
    }

}
