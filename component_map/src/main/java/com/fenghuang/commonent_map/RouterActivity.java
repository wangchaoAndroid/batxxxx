package com.fenghuang.commonent_map;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
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

import java.util.ArrayList;
import java.util.List;

public class RouterActivity extends BaseActivity {
    private AMap mAMap;
    private MapView mMapView;
    public MyLocationStyle myLocationStyle;
    private Polyline polyline;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private List<LatLng> latLngs = new ArrayList<LatLng>();
    @Override
    protected void initView() {
        mMapView = (MapView)findViewById(R.id.router_map);
        mAMap = mMapView.getMap();
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
    }

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

}
