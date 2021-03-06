package com.fenghuang.commonent_map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.fenghuang.commonent_map.bean.RouterModel;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private RadioButton mStart,mEnd;
    private RadioGroup mRadioGroup;
    private Handler mHandler = new Handler();

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getPosition();
            mHandler.postDelayed(this, 1000 * 60);
        }
    };
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
        myLocationStyle.showMyLocation(false);
        // 自定义定位蓝点图标
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
//                fromResource(R.drawable.navi_map_gps_locked));
        // 自定义精度范围的圆形边框颜色
//        myLocationStyle.strokeColor(android.R.color.transparent);
//        //自定义精度范围的圆形边框宽度
//        myLocationStyle.strokeWidth(0);
//        // 设置圆形的填充颜色
//        myLocationStyle.radiusFillColor(android.R.color.transparent);
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        mUiSettings = mAMap.getUiSettings();//实例化UiSettings类对象
//        mUiSettings.setMyLocationButtonEnabled(false);
        mAMap.setMyLocationEnabled(false);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        initLocation(this);
//        setAMapLocationListener();
//        startLocation();
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mHandler.post(mRunnable);

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
        mHandler.removeCallbacks(mRunnable);
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

    private TimePickerView pvTime;
    public static final int START = 0X001;
    public static final int END = 0X002;
    private String startTime , endTime;
    private int seleted;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.start_router){
            if(!isOpen){
                pvTime = new TimePickerBuilder(RouterActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                      // getRouter("2018-07-03 00:00:00","2018-08-12 23:00:00");

                       getRouter(startTime,endTime);
                    }
                })
                        .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                            @Override
                            public void customLayout(View v) {
                                final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                                ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                                mStart = v.findViewById(R.id.start_time);
                                mEnd = v.findViewById(R.id.end_time);
                                mRadioGroup = v.findViewById(R.id.rg_time);
                                tvSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(TextUtils.isEmpty(startTime)){
                                            RxToast.error("请选择开始时间");
                                            return;
                                        }
                                        if(TextUtils.isEmpty(endTime)){
                                            RxToast.error("请选择结束时间");
                                            return;
                                        }
                                        pvTime.returnData();
                                    }
                                });
                                ivCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvTime.dismiss();
                                    }
                                });

                                mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                                    @Override
                                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                        if(i == mStart.getId()){
                                            seleted = START;
                                            mStart.setHintTextColor(getResources().getColor(R.color.selectd));
                                            mEnd.setHintTextColor(Color.GRAY);
                                        }else {
                                            seleted = END;
                                            mEnd.setHintTextColor(getResources().getColor(R.color.selectd));
                                            mStart.setHintTextColor(Color.GRAY);
                                        }
                                    }
                                });
                                mStart.setChecked(true);
                                mStart.setHintTextColor(getResources().getColor(R.color.selectd));
                                mEnd.setHintTextColor(Color.GRAY);
                            }
                        })
                        .setType(new boolean[]{true, true, true, true, true, true})
//                        .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                        .setDividerColor(Color.DKGRAY)
                        .setContentTextSize(20)
                        .setBackgroundId(0xFFFFFFFF)
                        .setLineSpacingMultiplier(1.2f)
                        .setContentTextSize(16)
                        .setOutSideCancelable(false)
                        .setTextXOffset(0, 0, 0, 40, 0, -40)
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setDividerColor(0xFF24AD9D)
                        .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                            @Override
                            public void onTimeSelectChanged(Date date) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format = simpleDateFormat.format(date);
                                if(seleted == START){
                                    mStart.setText(format + "");
                                    startTime = format;
                                }else if(seleted == END){
                                    mEnd.setText(format + "");
                                    endTime = format;
                                }
                            }
                        })
                        .build();

                pvTime.show();
            }else {
                startRouter(null);
            }

        }
    }



    /**
     * 这里需要改用网络请求
     */
    private void startRouter(List<RouterModel> routerModels) {
        isOpen = !isOpen;
        if(isOpen){
            start_router.setText("关闭轨迹");
            latLngs.clear();
            for(RouterModel routerModel : routerModels){
                latLngs.add(new LatLng(routerModel.latitude,routerModel.longitude));
            }
//            latLngs.add(new LatLng(22.529996f,113.95896f));
//            latLngs.add(new LatLng(22.533117f,113.95185f));
//            latLngs.add(new LatLng(22.534565f,113.95271f));
//            latLngs.add(new LatLng(22.536290f,113.95356f));
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
        }else {
            if(polyline != null){
                polyline.remove();
            }
            start_router.setText("开启轨迹");

        }



    }

    /**
     * 获取当前位置
     */
    public void getPosition(){
        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        mMapNetServices.getLocation(token)
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



    /**
     * 获取轨迹
     */
    public void getRouter(String startTime ,String endTime) {

        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            return;
        }
        mMapNetServices.getNavigation(token,startTime,endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<List<RouterModel>>>() {
                    @Override
                    public void onSuccess(BaseEntery<List<RouterModel>> value) {
                        ILog.e(TAG,value.toString());
                        if(pvTime != null){
                            pvTime.dismiss();
                        }
                        List<RouterModel> routerModels = value.obj;
                        if(routerModels != null && !routerModels.isEmpty()){
                            startRouter(routerModels);
                        }else {
                            RxToast.error("无轨迹数据");
                        }

                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
                    }
                });
    }
}
