package com.fenghuang.component_user;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_user.bean.FenchModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SetFenchActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SetFenchActivity";
    private AMap mAMap;
    private MapView mMapView;
    public MyLocationStyle myLocationStyle;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    GeocodeSearch geocoderSearch;
    private TextView showAddress;
    private EditText setRaduisEt;
    public AMapLocationClient mLocationClient ;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;
    NetServices  netServices= RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    @Override
    protected void initView() {
        mMapView = (MapView)findViewById(R.id.map);
        mAMap = mMapView.getMap();
        showAddress= findViewById(R.id.address_name);
        setRaduisEt = findViewById(R.id.fench_radius);
    }

    class CurrenetPos {
        String address;
        double lat;
        double lngt;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
    }

    @Override
    protected void setEvent() {
        initMap();
        initLocation(this);
//        setAMapLocationListener();
        //startLocation();
        addOnClickListeners(R.id.set_fench,R.id.back);
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

    //声明定位回调监听器
    public AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude())));
                    mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    //开始定位
    public void startLocation(){
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    CurrenetPos currenetPos;

    public void initMap(){
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                RxToast.info(latLng.longitude + "----" + latLng.latitude);
                LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude,latLng.longitude);
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                        String province = regeocodeAddress.getProvince();
                        String city = regeocodeAddress.getCity();
                        String district = regeocodeAddress.getDistrict();
                        String formatAddress = regeocodeAddress.getFormatAddress();
                        String replace = formatAddress.replace(province, "").replace(city, "").replace(district, "");
                        ILog.e("1111111","regeocodeResult" + replace +"----rCode" + rCode);
                        showAddress.setText(replace + "");
                        currenetPos = new CurrenetPos();
                        currenetPos.address = replace;
                        currenetPos.lat = latLng.latitude;
                        currenetPos.lngt= latLng.longitude;
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {

                    }
                });
            }
        });
        geocoderSearch = new GeocodeSearch(this);

        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
//        myLocationStyle.strokeColor(android.R.color.transparent);
//        //自定义精度范围的圆形边框宽度
//        myLocationStyle.strokeWidth(0);
//        // 设置圆形的填充颜色
//        myLocationStyle.radiusFillColor(android.R.color.transparent);
//        myLocationStyle.showMyLocation(true);
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        mUiSettings = mAMap.getUiSettings();//实例化UiSettings类对象
//        mUiSettings.setMyLocationButtonEnabled(false);

        mAMap.setMyLocationEnabled(false);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        getPosition();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_fench;
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.set_fench){
            String trim = setRaduisEt.getText().toString().trim();
            if(!TextUtils.isEmpty(trim) && currenetPos != null){
                int raduis =Double.valueOf(trim).intValue();
                postFenchData(raduis,currenetPos);
            }else {
                RxToast.error("设置失败，请确认围栏信息");
            }
        }else if(id == R.id.back){
            finish();
        }
    }

    private void postFenchData(int raduis,CurrenetPos currenetPos) {
        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        netServices.setEnclosure(token,currenetPos.lngt,currenetPos.lat,raduis).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {
                        RxToast.info("设置成功");
                        finish();

                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e +"");
                    }
                });
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);

        super.finish();
    }

    /**
     * 获取当前位置
     */
    public void getPosition(){
        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        netServices.getLocation(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<FenchModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<FenchModel> value) {
                        FenchModel fenchModel = value.obj;
                        if(fenchModel != null){
                            ILog.e(TAG,fenchModel.latitude   +  "----"  + fenchModel.longitude);
                            LatLng latLng = new LatLng(fenchModel.latitude,fenchModel.longitude);
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

                    }
                });
    }

}
