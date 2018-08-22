package com.fenghuang.component_user;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.billy.cc.core.component.CC;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.ActivityStackManager;
import com.fenghuang.component_user.bean.BindModel;
import com.fenghuang.component_user.login.LoginActivity;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "SettingActivity";
    private static final int FROM_SHOP = 0x01;
    public static final int FROM_CHARGE = 0x02;
    private TextView scan_tv;
    private TextView logoutTv;
    NetServices netServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    @Override
    protected void initView() {
        scan_tv = (TextView) findViewById(R.id.scan_tv);
        logoutTv = findViewById(R.id.btn_logout);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setEvent() {
        addOnClickListeners(R.id.scan_tv,R.id.btn_logout,R.id.tv_nearbyShop,R.id.tv_charge,R.id.set_fench,R.id.un_bind,R.id.back);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.scan_tv) {
            CC.obtainBuilder("component_mall")
                    .setContext(this)
                    .setActionName("showActivityA")
                    .build()
                    .call();
        } else if (id == R.id.btn_logout) {
            logout();
        } else if (id == R.id.tv_nearbyShop) {
            getNearbyShop();
        }else if(id == R.id.tv_charge){
            getNearbyCharged();
        }else if(id == R.id.set_fench){
            setFench();
        }else if(id == R.id.un_bind){
            unBind();
        }else if(id == R.id.back){
            finish();
        }
    }

    /**
     * 退出登录
     */
    public void logout(){
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            return;
        }
        netServices.logout(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {
                        ILog.e( "logout" ,value + "");
                        //清除内存保存用户数据
                        UserManager.clearUserInfo();
                        //清除sp token
                        SPDataSource.put(SettingActivity.this,SPDataSource.USER_TOKEN,"");
                        //注销信鸽
                        XGPushManager.unregisterPush(SettingActivity.this);
                        //退到登录界面
                        startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                        ActivityStackManager.getInstance().finishAllActivity();
                    }

                    @Override
                    public void onFailture(String e) {
                        ILog.e( "logout" ,e + "");
                        RxToast.error(e + "");
                    }
                });
    }


    /**
     * 设置围栏信息
     */
    private void setFench() {
        startActivity(new Intent(SettingActivity.this,SetFenchActivity.class));
    }


    /**
     * 附近门店
     */
    public void getNearbyShop() {
        showPickerView(FROM_SHOP);
    }


    private List<RangeWrapper> ranges = new ArrayList<>();
    /**
     * 弹出选择器
     */
    private void showPickerView(int from) {
        ranges.add(new RangeWrapper("500米内",500));
        ranges.add(new RangeWrapper("1000米内",1000));
        ranges.add(new RangeWrapper("2000米内",2000));
        OptionsPickerView pvOptions = new OptionsPickerBuilder(SettingActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if(ranges != null && !ranges.isEmpty()){
                    ILog.e(TAG,options1 + "---" + options2 + "---" + options3);
                    RangeWrapper rangeWrapper = ranges.get(options1);
                    int rangeMeter = rangeWrapper.rangeMeter;
                    Bundle bundle = new Bundle();
                    bundle.putInt("range",rangeMeter);
                    switch (from){
                        case FROM_CHARGE:
                            ChargeActivity.startAction(SettingActivity.this,bundle);
                            break;
                        case FROM_SHOP:
                            NeiboorhoorActivity.startAction(SettingActivity.this,bundle);
                            break;
                    }

                }
            }
        })
                .setTitleText("范围")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(14)
                .build();

        pvOptions.setPicker(ranges);//一级选择器
        //pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 获取附近充电桩
     */
    public void getNearbyCharged() {
        showPickerView(FROM_CHARGE);
    }

    /**
     * 解除绑定
     */
    private void unBind() {
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            return;
        }
        netServices.getViceCardAllByAccount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<BindModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<BindModel> value) {
                        //showPickerView();
                    }

                    @Override
                    public void onFailture(String e) {

                    }
                });
    }

}
