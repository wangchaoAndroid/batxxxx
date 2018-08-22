package com.fenghuang.commonent_map;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.fenghuang.component_base.base.BaseActivity;
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

public class RouterActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "RouterActivity";
    private AMap mAMap;
    private MapView mMapView;
    public MyLocationStyle myLocationStyle;
    private Polyline polyline;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private TextView start_router;
    private List<LatLng> latLngs = new ArrayList<LatLng>();
    MapNetServices mMapNetServices = RetrofitManager.getInstance().initRetrofit().create(MapNetServices.class);
    private boolean isOpen;
    public AMapLocationClient mLocationClient ;
    public AMapLocationClientOption mLocationOption;
    @Override
    protected void initView() {
        mMapView = (MapView)findViewById(R.id.router_map);
        start_router = (TextView)findViewById(R.id.start_router);
        start_router.setText("开启轨迹");
        mAMap = mMapView.getMap();
        addOnClickListeners(R.id.start_router);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.showMyLocation(true);
        // 自定义定位蓝点图标
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
//                fromResource(R.drawable.navi_map_gps_locked));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(android.R.color.transparent);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mUiSettings = mAMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(true);
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        initLocation(this);
        setAMapLocationListener();
        startLocation();
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
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

    //声明定位回调监听器
    public AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude())));
                    mLocationClient.stopLocation();
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    @Override
    protected void setEvent() {}

    @Override
    protected int getLayoutId() {
        return R.layout.activity_router;
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationClient.onDestroy();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.start_router){
            startRouter();
        }
    }

    /**
     * 这里需要改用网络请求
     */
    private void startRouter() {
        isOpen = !isOpen;
        if(isOpen){
            start_router.setText("关闭轨迹");
            String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
            latLngs.clear();
            latLngs.add(new LatLng(22.529996f,113.95896f));
            latLngs.add(new LatLng(22.533117f,113.95185f));
            latLngs.add(new LatLng(22.534565f,113.95271f));
            latLngs.add(new LatLng(22.536290f,113.95356f));
            if(polyline != null){
                polyline.remove();
            }
            LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
            polyline =mAMap.addPolyline(new PolylineOptions().
                    addAll(latLngs).width(7).color(Color.argb(255, 1, 1, 255)));
            for (int i = 0; i < latLngs.size(); i++) {
                newbounds.include(latLngs.get(i));
            }
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(),
                    30));//第二个参数为四周留空宽度
            ILog.e(TAG,token);
        }else {
            if(polyline != null){
                polyline.remove();
            }
            start_router.setText("开启轨迹");

        }



    }
}
