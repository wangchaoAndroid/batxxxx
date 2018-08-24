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
import com.contrarywind.interfaces.IPickerViewData;
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

public class AccountAndSafeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "AccountAndSafeActivity";
    private TextView modifyPwd;
    private TextView logoutTv;
    NetServices netServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    protected void setEvent() {
        addOnClickListeners(R.id.modify_pwd,R.id.un_bind,R.id.back);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_and_safe;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.modify_pwd) {
            modifyPwd();
        }else if(id == R.id.un_bind){
            getUnBindData();
        }else if(id == R.id.back){
            finish();
        }
    }

    /**
     * 修改密码
     */
    private void modifyPwd() {
        startActivity(SettingNewPwdActivity.class,false);
    }

    /**
     * 弹出选择器
     */
    private void showPickerView(List<? extends IPickerViewData> list , String title) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(AccountAndSafeActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if(list != null && !list.isEmpty()){
                    ILog.e(TAG,options1 + "---" + options2 + "---" + options3);
                    IPickerViewData iPickerViewData = list.get(options1);
                    if(iPickerViewData instanceof  RangeWrapper){

                    }else if(iPickerViewData instanceof BindModel){
                        //弹出确认对话框
                        AlertDialog.Builder  builder = new AlertDialog.Builder(AccountAndSafeActivity.this)
                                .setTitle("解除绑定？");

                        AlertDialog dialog = builder.create();
                        builder .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        BindModel bindModel = (BindModel) iPickerViewData;
                                        unBind(bindModel.account);
                                    }});
                        dialog.show();

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
        pvOptions.show();
    }



    /**
     * 获取解绑数据
     */
    private void getUnBindData() {
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            return;
        }
        netServices.getViceCardAllByAccount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<List<BindModel>>>() {
                    @Override
                    public void onSuccess(BaseEntery<List<BindModel>> value) {
                        if(value != null){
                            List<BindModel> bindModels = value.obj;
                            if(bindModels != null &&  !bindModels.isEmpty()){
                                List<RangeWrapper> ids = new ArrayList<>();
                                showPickerView(ids,"选择编号");
                            }else {
                                RxToast.info("暂无数据");
                            }
                        }

                    }

                    @Override
                    public void onFailture(String e) {

                    }
                });
    }


    /**
     * 解绑
     */
    private void unBind(String acount) {
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            return;
        }
        netServices.unBind(token,acount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {

                    }

                    @Override
                    public void onFailture(String e) {

                    }
                });
    }

}
