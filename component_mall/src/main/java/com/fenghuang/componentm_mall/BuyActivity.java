package com.fenghuang.componentm_mall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.billy.cc.core.component.CC;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.componentm_mall.bean.Product;
import com.fenghuang.componentm_mall.camera.CameraActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/7/21 11:04
 */
public class BuyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "BuyActivity";
    private ImageView mImageView;
    MallNetServices mMallNetServices = RetrofitManager.getInstance().initRetrofit().create(MallNetServices.class);
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.mall_enter_camera_iv){
            startActivityForResult(new Intent(this, CameraActivity.class),2);
        }else if(id == R.id.top_back){
            finish();
        }else if(id == R.id.btn_buy){
            pay("10086");
        }
    }

    @Override
    protected void initView() {
        mImageView = findViewById(R.id.mall_enter_camera_iv);
        addOnClickListeners(R.id.mall_enter_camera_iv,R.id.top_back,R.id.btn_buy);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
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
        if(requestCode == RESULT_OK){
            if(requestCode == 2 ){
                String productNumber =data.getStringExtra("productNumber");
            }
        }

    }

    private void pay(final String productNumber) {
        final String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        mMallNetServices.purchase(token,"10086",productNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {

                        bindBattery(token,productNumber);
                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
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
                        CC.obtainBuilder("component_app")
                                .setContext(BuyActivity.this)
                                .setActionName("enterMain")
                                .build()
                                .call();
                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
                    }
                });
    }
}
