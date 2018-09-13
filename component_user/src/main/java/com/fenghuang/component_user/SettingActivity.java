package com.fenghuang.component_user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.contrarywind.interfaces.IPickerViewData;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.ActivityStackManager;
import com.fenghuang.component_base.view.RxDialog;
import com.fenghuang.component_base.view.RxDialogSure;
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
    public static final int FROM_UNBIND = 0x02;
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
        ranges.add(new RangeWrapper("500米内",500));
        ranges.add(new RangeWrapper("1000米内",1000));
        ranges.add(new RangeWrapper("2000米内",2000));
    }

    @Override
    protected void setEvent() {
        addOnClickListeners(R.id.scan_tv,R.id.btn_logout,R.id.tv_nearbyShop,R.id.tv_charge,R.id.set_fench,R.id.back,R.id.acount_and_safe);
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
            if(toLoginForToken()){
                setFench();
            }
        }else if(id == R.id.back){
            finish();
        }else if(id == R.id.acount_and_safe){
            if(toLoginForToken()){
                startActivity(AccountAndSafeActivity.class,false);
            }
        }
    }


    /**
     * 退出登录
     */
    public void logout(){
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            RxToast.error("您还未登录");
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
                        ActivityStackManager.getInstance().popAllActivity();
                        startActivity(new Intent(SettingActivity.this,LoginActivity.class));

                    }

                    @Override
                    public void onFailture(String e) {
                        ILog.e( "logout" ,e + "");
                        RxToast.error(e + "");
                    }
                });
    }

    public boolean toLoginForToken(){
        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            CCResult ccResult = CC.obtainBuilder("component_user")
                    .setContext(this)
                    .setActionName("toLoginActivityForToken")
                    .build()
                    .call();
            String data = ccResult.getDataItem(SPDataSource.USER_TOKEN);
            if(!TextUtils.isEmpty(data)){
                return true;
            }
            return false;

        }
        return true;
    }


    /**
     * 设置围栏信息
     */
    private void setFench() {
        startActivityForResult(new Intent(SettingActivity.this,SetFenchActivity.class),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10  &&  resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }

    }

    /**
     * 附近门店
     */
    public void getNearbyShop() {
        showPickerView(FROM_SHOP,ranges,"范围");
    }


    private List<RangeWrapper> ranges = new ArrayList<>();
    /**
     * 弹出选择器
     */
    private void showPickerView(int from, List<? extends IPickerViewData> list , String title) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(SettingActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if(list != null && !list.isEmpty()){
                    ILog.e(TAG,options1 + "---" + options2 + "---" + options3);
                    IPickerViewData iPickerViewData = list.get(options1);
                    if(iPickerViewData instanceof  RangeWrapper){
                        RangeWrapper rangeWrapper = (RangeWrapper) iPickerViewData;
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
            }
        })
                .setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(14)
                .build();

        pvOptions.setPicker(list);//一级选择器
        //pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 获取附近充电桩
     */
    public void getNearbyCharged() {
        showPickerView(FROM_CHARGE,ranges,"范围");
    }

}
