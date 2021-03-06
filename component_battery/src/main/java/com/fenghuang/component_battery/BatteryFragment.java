package com.fenghuang.component_battery;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.billy.cc.core.component.CCResult;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.download.DownloadManager;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.view.RxDialog;
import com.fenghuang.component_base.view.RxDialogSure;
import com.fenghuang.component_battery.adapter.BannerAdapter;
import com.fenghuang.component_battery.bean.Ad;
import com.fenghuang.component_battery.bean.FenchModel;
import com.fenghuang.component_battery.bean.HomeModel;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/7/18 10:26
 */
public class BatteryFragment  extends LazyLoadFragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "BatteryFragment";
    private MapView mMapView;
    private ViewPager mBannerView;
    private TextView count_down_time,title_warn;
    List<Ad> items = new ArrayList<>();
    private BannerAdapter mBannerAdapter;
    private TextView lastPrecent;
    private Spinner spinner;
    private ArrayAdapter arr_adapter;
    private ArrayList<String> mBind_ids = new ArrayList<>();
    private ImageView left1,left2,right1,right2,iv_battery;
    BatteryNetServices batteryNetServices = RetrofitManager.getInstance().initRetrofit().create(BatteryNetServices.class);
    HomeModel homeModel;
    private  LinearLayout pointView;;

    //定义Handler接收发送消息
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
                if (msg.what == 999) {
                //获取viewPager当前的位置
                int currentItem = mBannerView.getCurrentItem();
                currentItem++;
                //设置viewPager的位置
                mBannerView.setCurrentItem(currentItem);
                // 继续轮播
                //Logger.i(TAG, "bobobo.....");//测试
                //调用发送消息的方法
                startRool();
            }
        };
    };



    private List<ImageView> dotsList = new ArrayList<>();
    private RxDialogSure mDialogSure;

    /**
     * 初始化小点
     */
    private void initDots() {

        //每次初始化之前清空集合
        dotsList.clear();
        // 每次初始化之前  移除  布局中的所
        // 有小点
        pointView.removeAllViews();
        for (int i = 0; i < items.size(); i++) {
            //创建小点点图片
            ImageView imageView = new ImageView(getActivity());
            Drawable drawable = null;
            if (i == 0) {
                // 亮色图片
                drawable = getResources().getDrawable(R.drawable.main_point);

            } else {
                drawable = getResources().getDrawable(R.drawable.main_point66);
            }
            imageView.setImageDrawable(drawable);
            // 考虑屏幕适配
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(getActivity(), 5), dip2px(getActivity(), 5));
            //设置小点点之间的间距
            params.setMargins(dip2px(getActivity(), 5), 0, dip2px(getActivity(), 5), 0);
            //将小点点添加大线性布局中
            pointView.addView(imageView, params);
            // 将小点的控件添加到集合中
            dotsList.add(imageView);
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    /**
     * 每隔2秒发送一次消息
     */
    private void startRool() {
        // 开始轮播
        handler.sendEmptyMessageDelayed(999, 3000);
    }

    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mMapView = view.findViewById(R.id.app_enter_map);
        mBannerView = view.findViewById(R.id.app_banner);
        count_down_time = view.findViewById(R.id.count_down_time);
        iv_battery = view.findViewById(R.id.iv_battery);
        title_warn = view.findViewById(R.id.title_warn);
        lastPrecent = view.findViewById(R.id.last_precent);
        left1 = view.findViewById(R.id.left1);
        left2 = view.findViewById(R.id.left2);
        right1 = view.findViewById(R.id.right1);
        right2 = view.findViewById(R.id.right2);
        spinner = (Spinner) findViewById(R.id.title_tv);
        pointView = findViewById( R.id.point_view);

        mAMap = mMapView.getMap();
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        addOnClickListeners(R.id.right1,R.id.right2);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_battery;
    }

    @Override
    protected void lazyLoad() {
        ILog.e(TAG,"lazyLoad");
        initMap();
        //适配器
        arr_adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mBind_ids);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
//        spinner.setSelection(0);

        getHomeInfo();

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                if(mBind_ids != null && !mBind_ids.isEmpty()){
                    String productNumber = parent.getItemAtPosition(position).toString();
                    //切换终端
                    ILog.e(TAG,productNumber + "");
                    changeDevice(productNumber);
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    public boolean toLoginForToken(){
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            CCResult ccResult = CC.obtainBuilder("component_user")
                    .setContext(getActivity())
                    .setActionName("toLoginActivityForToken")
                    .build()
                    .call();
            String data = ccResult.getDataItem(SPDataSource.USER_TOKEN);
            if(!TextUtils.isEmpty(data)){
                getHomeInfo();
                return true;
            }
            return false;

        }
        return true;
    }

    /**
     * 切换电池终端
     */
    private void changeDevice(String productNumber) {
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            return;
        }
        batteryNetServices.bindingDefault(token,productNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {
//                        RxToast.info("切换成功");
                        getHomeInfo();
                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
                    }
                });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int  viewId = view.getId();
        if(!toLoginForToken()){
           return;
        }
        if(viewId == R.id.right1){
            if(homeModel != null){
                int lockstatus = homeModel.lockstatus;
                if(lockstatus == 0){
                    switchLock(1);
                }else {
                    switchLock(0);
                }
            }

        }else if(viewId == R.id.right2){
            //开关  0关 1开 2充电中 3欠费关机
            if(homeModel != null){
                int batteryStatus = homeModel.batteryStatus;
                if(batteryStatus == 0){
                    switchBattery(1);
                }else {
                    switchBattery(0);
                }

            }
            getHomeInfo();

        }else if(viewId == R.id.tv_sure){
            if(mDialogSure != null && mDialogSure.isShowing()){
                mDialogSure.dismiss();
            }
            pay();
        }
    }
    //调用位置死锁
    private void switchLock(final int staus) {
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            return;
        }
        batteryNetServices.switchEnclosure(token,staus,2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<FenchModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<FenchModel> value) {
                        if(staus == 1){
                            RxToast.info("位置已锁定");
                            right1.setImageResource(R.mipmap.lock);
                        }else {
                            RxToast.info("位置解除锁定");
                            right1.setImageResource(R.mipmap.un_lock);
                        }

                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.info(e + "");
                    }
                });
    }
    //开关电池
    private void switchBattery(final int status) {
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            return;
        }
        batteryNetServices.switchBattery(token,status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {

                       // 2 充电中 3分期付款需要缴费 4电池处于离线状态 5成功发出指令
                        ILog.e(TAG,value.toString());
                        int code =  Integer.valueOf((String) value.obj);
                        if(code == 5){
                            RxToast.info(value.msg);
                            right2.setImageResource(R.mipmap.un_open);
                        }else {
                            RxToast.error(value.msg);
                        }
                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.info(e + "");
                    }
                });
    }

    public void getHomeInfo(){
        ILog.e(TAG,"1111111111111");
        showLoadingDialog();
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        batteryNetServices.getHome(token).subscribeOn(Schedulers.io())
                .compose(this.<BaseEntery<HomeModel>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<HomeModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<HomeModel> value) {
                        dimissLoadingDialog();
                        ILog.e(TAG,value + "");
                        if(value != null){
                            items.clear();
                            mBind_ids.clear();
                            homeModel = value.obj;
                            String defaultProductNumber = homeModel.defaultProductNumber;
                            List<HomeModel.ViceCard> viceCardList = homeModel.viceCardList;
                            if(viceCardList != null && !viceCardList.isEmpty()){
                                for(HomeModel.ViceCard viceCard : viceCardList){
                                    String number  =  viceCard.productNumber;
                                    mBind_ids.add(number);
                                    if(defaultProductNumber.equals(number)){
                                        spinner.setSelection(mBind_ids.indexOf(number));
                                    }
                                }
                                arr_adapter.notifyDataSetChanged();
                            }


                            HomeModel.Alarmtab alarmtab = homeModel.alarmtab;
                            if(alarmtab != null){
                                count_down_time.setText(alarmtab.alarmcontent + "");
                                title_warn.setText(alarmtab.alarmtitle + "");
                            }
                            // 围栏
                            if(homeModel.railstatus == 1){
                                left1.setVisibility(View.VISIBLE);
                            }else {
                                left1.setVisibility(View.GONE);
                            }
                            //告警
                            if(homeModel.alarmtabStatus  == 1){
                                left2.setVisibility(View.VISIBLE);
                            }else {
                                left2.setVisibility(View.GONE);
                            }
                            //位置死锁
                            if(homeModel.lockstatus == 1){
                                right1.setImageResource(R.mipmap.lock);
                            }else {
                                right1.setImageResource(R.mipmap.un_lock);
                            }

                            //充电中
                            if(homeModel.isCharge == 1){
//                                right2.setImageResource(R.mipmap.un_open);
                                iv_battery.setImageResource(R.mipmap.charging);
                            }
                            //开关  0关 1开 2充电中 3欠费关机
                            if(homeModel.batteryStatus  == 0){
                                right2.setImageResource(R.mipmap.un_open);
                                if(homeModel.isCharge != 1){
                                    iv_battery.setImageResource(R.drawable.ba_cloose);
//                                    int electricquantity = homeModel.electricquantity;
//                                    lastPrecent.setText(electricquantity + "%");
                                    lastPrecent.setVisibility(View.GONE);
                                }

                            }else if(homeModel.batteryStatus ==1){
                                right2.setImageResource(R.mipmap.open);

                                if(homeModel.isCharge != 1){
                                    lastPrecent.setVisibility(View.VISIBLE);
                                    int electricquantity = homeModel.electricquantity;
                                    lastPrecent.setText(electricquantity + "%");
                                    if(electricquantity <= 10){
                                        iv_battery.setImageResource(R.mipmap.p_10);
                                    }else if(electricquantity <= 25){
                                        iv_battery.setImageResource(R.mipmap.p_25);
                                    }else if(electricquantity <= 45){
                                        iv_battery.setImageResource(R.mipmap.p_45);
                                    }else if(electricquantity <= 60){
                                        iv_battery.setImageResource(R.mipmap.p_60);
                                    }else if(electricquantity < 100){
                                        iv_battery.setImageResource(R.mipmap.p_80);
                                    }else {
                                        iv_battery.setImageResource(R.mipmap.p_100);
                                    }
                                }

                            }else if(homeModel.batteryStatus ==2){
                                //修改后这里不在是充电中
                            }else if(homeModel.batteryStatus ==3){
                                right2.setImageResource(R.mipmap.un_open);
                                right1.setImageResource(R.mipmap.un_lock);
                                iv_battery.setImageResource(R.drawable.ba_cloose);
                                mDialogSure = new RxDialogSure(getActivity());
                                mDialogSure.setCancelable(false);
                                mDialogSure.setContent("您的电池属于分期付款类型，本期欠费未缴清，是否立即缴费（注：欠费无法正常使用）.");
                                mDialogSure.setTitle("警告");
                                mDialogSure.setSure("确认缴费");
                                mDialogSure.show();
                                mDialogSure.setSureListener(BatteryFragment.this);
                            }

                            List<Ad> advertisingtabList = homeModel.advertisingtabList;
                            if(advertisingtabList != null && !advertisingtabList.isEmpty()){
                                items.clear();
                                items.addAll(advertisingtabList);
                                Log.e("1111","mBannerAdapter" +mBannerAdapter);
                                if(mBannerAdapter == null){
                                    mBannerAdapter = new BannerAdapter(getActivity(),items);
                                    mBannerView.setAdapter(mBannerAdapter);
                                    mBannerView.setOnPageChangeListener(BatteryFragment.this);
                                    mBannerAdapter.notifyDataSetChanged();
                                    mBannerView.setCurrentItem(items.size() * 10000);
                                    initDots();
                                    startRool();
                                }

                            }

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
                        dimissLoadingDialog();
                        RxToast.error(e);
                        ILog.e(TAG,"1111111111111" +e);
                    }
                });
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("11111","onDestroy");
        mBannerAdapter = null;
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
        homeModel = null;
        if(mMapView != null){
            mMapView.onDestroy();
        }

    }

    @Override
    protected void stopLoad() {
        Log.e("1111","stopLoad");
        super.stopLoad();
        mBannerAdapter = null;
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
        homeModel = null;
        if(mMapView != null){
            mMapView.onDestroy();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        ILog.e(TAG,"isLoad" + isLoad);
        if(!isLoad){
            getHomeInfo();
        }
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
                if(toLoginForToken()){
                    CC.obtainBuilder("component_map")
                            .setActionName("enter_router_map")
                            .build()
                            .call();
                }
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private boolean isPausing;
    @Override
    public void onStop() {
        super.onStop();
        isPausing = true;
        isLoad = false;
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onStart() {
        super.onStart();
//        if(isPausing){
//            startRool();
//            isPausing= false;
//        }
    }

    @Override
    public void onPageSelected(int position) {
//遍历存放图片的数组
        if(items.isEmpty() || dotsList.isEmpty()){
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            ImageView iv = (ImageView) dotsList.get(i);
            Drawable drawable = null;
            //判断小点点与当前的图片是否对应，对应设置为亮色 ，否则设置为暗色
            if (i == position % items.size()) {
                drawable = getResources().getDrawable(R.drawable.main_point);
                iv.setImageDrawable(drawable);
            } else {
                drawable = getResources().getDrawable(R.drawable.main_point66);
                iv.setImageDrawable(drawable);

            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 缴费
     */
    public void pay(){
        final String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        String productNumber =  spinner.getSelectedItem().toString().trim();
        ILog.e(TAG,productNumber + "");
        if(TextUtils.isEmpty(productNumber)){
            RxToast.error("编号不存在");
            return;
        }
        showLoadingDialog();
        batteryNetServices.purchase(200,token,productNumber,0,0,2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<String>>() {
                    @Override
                    public void onSuccess(BaseEntery<String> value) {
                        dimissLoadingDialog();
                        PingppUI.createPay(getActivity(), value.obj, new PaymentHandler() {
                            @Override public void handlePaymentResult(Intent data) {
                                int code = data.getExtras().getInt("code");
                                String result = data.getExtras().getString("result");
                                ILog.e(TAG, "handlePaymentResult: "+ code + "---" + result );
                                if(code == 1){ //成功
                                    RxToast.showToast("缴费成功");
                                   // getHomeInfo();

                                }else {
                                    RxToast.error(""+ result);
                                }
                            }
                        });
                        //bindBattery(token,productNumber);

                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
                        dimissLoadingDialog();
                    }
                });
    }
}
