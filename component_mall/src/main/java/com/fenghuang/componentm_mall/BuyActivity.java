package com.fenghuang.componentm_mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.componentm_mall.adapter.PayTypeAdapter;
import com.fenghuang.componentm_mall.bean.PayType;
import com.fenghuang.componentm_mall.bean.Product;
import com.fenghuang.componentm_mall.camera.CameraActivity;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/7/21 11:04
 */
public class BuyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BuyActivity";
    private ImageView mImageView;
    MallNetServices mMallNetServices = RetrofitManager.getInstance().initRetrofit().create(MallNetServices.class);
    private String productNumber;
    private TextView numberTv;
    private RecyclerView payTypeRv;
    private int request = 2;
    private List<PayType> types = new ArrayList<>();
    private PayTypeAdapter payTypeAdapter;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.mall_enter_camera_iv){
            startActivityForResult(new Intent(this, CameraActivity.class),request);
        }else if(id == R.id.top_back){
            finish();
        }else if(id == R.id.btn_buy){
            pay();
        }
    }

    @Override
    protected void initView() {
        mImageView = findViewById(R.id.mall_enter_camera_iv);
        numberTv = findViewById(R.id.num_tv);
        payTypeRv = findViewById(R.id.payTypeRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        payTypeRv.setLayoutManager(linearLayoutManager);

        addOnClickListeners(R.id.mall_enter_camera_iv,R.id.top_back,R.id.btn_buy);

    }

    private PayType checkedItem;

    @Override
    protected void initData(Bundle savedInstanceState) {
        payTypeAdapter = new PayTypeAdapter(R.layout.item_pay_type,types);
        payTypeRv.setAdapter(payTypeAdapter);
        payTypeAdapter.setOnCustomCheckedListener(new PayTypeAdapter.OnCustomCheckedListener() {
            @Override
            public void onCbClick( PayType item) {
                checkedItem = item;
            }
        });
    }

    @Override
    protected void setEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_buy;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == request ){
                productNumber = data.getStringExtra("productNumber");
                numberTv.setText(productNumber + "");
            }
            getPayType(productNumber);

        }

    }

    public void getPayType(String num){
        mMallNetServices.getStageType(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<List<PayType>>>() {

                    @Override
                    public void onSuccess(BaseEntery<List<PayType>> value) {
                        List<PayType> payTypes = value.obj;
                        types.clear();
                        if(payTypes != null && !payTypes.isEmpty()){
                            types.addAll(payTypes);
                        }
                        payTypeAdapter.notifyDataSetChanged();
                        for(PayType payType : payTypes){
                            ILog.e(TAG,"type" + payType.toString());
                        }

                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
                    }
                });
    }

    private void pay() {
        if(alertDialog != null && alertDialog.isShowing()){
            return;
        }
        showLoadingDialog();
        final String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        int id = checkedItem.id;
        int isStage;
        if(id == -1){
            isStage = 0;
        }else {
            isStage = 1;
        }
        mMallNetServices.purchase(200,token,productNumber,isStage,checkedItem.alarmnum,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<String>>() {
                    @Override
                    public void onSuccess(BaseEntery<String> value) {
                        dimissLoadingDialog();
                        PingppUI.createPay(BuyActivity.this, value.obj, new PaymentHandler() {
                            @Override public void handlePaymentResult(Intent data) {
                                int code = data.getExtras().getInt("code");
                                String result = data.getExtras().getString("result");
                                ILog.e(TAG, "handlePaymentResult: "+ code + "---" + result );
                                if(code == 1){ //成功
                                    RxToast.showToast("购买成功");
                                    CC.obtainBuilder("component_app")
                                .setContext(BuyActivity.this)
                                .setActionName("enterMain")
                                .build()
                                .call();
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

    private void bindBattery(String token ,String productNumber) {
        mMallNetServices.bindBattery(token,productNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {
                        RxToast.showToast("绑定电池成功");

                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
                    }
                });
    }
}
