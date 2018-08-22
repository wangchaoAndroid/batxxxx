package com.fenghuang.component_user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.helper.SpacesItemDecoration;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_user.adapter.NeiborAdapter;
import com.fenghuang.component_user.bean.Neiboor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/8/20 17:19
 */
public class ChargeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "WarnActivity";
    private RecyclerView mRecyclerView;

    private ImageView back;
    List<Neiboor> mNeiboors = new ArrayList<>();
    private NeiborAdapter mNeiborAdapter;
    NetServices batteryNetServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    private int mRange;
    public static void startAction(Context context ,Bundle bundle){
        Intent intent = new Intent(context,ChargeActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        back = findViewById(R.id.back);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        back.setOnClickListener(this);
        mNeiborAdapter = new NeiborAdapter(R.layout.item_neibor,mNeiboors);
        mRecyclerView.setAdapter(mNeiborAdapter);
        mNeiborAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //标记为已读
                if(!mNeiboors.isEmpty()){
                    Neiboor neiboor = mNeiboors.get(position);
                }

            }
        });
    }

    /**
     * 获取附近充电桩
     */
    public void getNeiborInfo(int neiborMeters,AMapLocation amapLocation){
        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        ILog.e(TAG,token);
        batteryNetServices.getNearbyCharge(token,neiborMeters,amapLocation.getLongitude(),amapLocation.getLatitude()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<List<Neiboor>>>() {
                    @Override
                    public void onSuccess(BaseEntery<List<Neiboor>> value) {
                        ILog.e(TAG,value + "");
                        if(value != null && !value.obj.isEmpty()){
                            mNeiboors.clear();
                            mNeiboors.addAll(value.obj);
                            mNeiborAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e);
                    }
                });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mRange = intent.getIntExtra("range", 0);
        startLocation();
    }



    @Override
    protected void setEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.back){
            finish();
        }
    }

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient ;
    public void initLocation(Context context){
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        //option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
    }
    //开始定位
    public void startLocation(){
        initLocation(this);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
            mLocationClient.setLocationListener(mAMapLocationListener);
        }
    }

    //声明定位回调监听器
    public AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    getNeiborInfo(mRange, amapLocation);
                    mLocationClient.unRegisterLocationListener(this);
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                    mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
}
