package com.fenghuang.component_user;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.MD5Util;
import com.fenghuang.component_base.utils.ViewFinder;
import com.fenghuang.component_user.login.LoginActivity;
import com.fenghuang.component_user.view.TimeCount;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/8/23 10:46
 */
public class SettingNewPwdActivity extends BaseActivity implements View.OnClickListener{
    private TextInputEditText tiet_oldPwd,tiet_newPed,tiet_re_newPwd;
    private NetServices mNetServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    @Override
    protected void initView() {
        ViewFinder viewFinder = new ViewFinder(this);
        tiet_oldPwd = viewFinder.find(R.id.tiet_oldPwd);
        tiet_newPed = viewFinder.find(R.id.tiet_newPwd);
        tiet_re_newPwd = viewFinder.find(R.id.tiet_re_password);
        addOnClickListeners(R.id.btn_sure,R.id.back);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_newpwd;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_sure) {
            String oldPwd = tiet_oldPwd.getText().toString().trim();
            String newPwd = tiet_newPed.getText().toString().trim();
            String re_password = tiet_re_newPwd.getText().toString().trim();
            if(TextUtils.isEmpty(oldPwd)){
                RxToast.error("请输入旧密码");
                return;
            }

            if(TextUtils.isEmpty(newPwd)){
                RxToast.error("请输入新密码");
                return;
            }

            if(TextUtils.isEmpty(re_password)){
                RxToast.error("请再次输入新密码");
                return;
            }

            if(!re_password.equals(newPwd)){
                RxToast.error("新密码输入不一致，请重新输入");
                return;
            }
            String token = UserManager.getToken();
            if(TextUtils.isEmpty(token)){
                return;
            }
            mNetServices.updatePassword(token, MD5Util.getStringMD5(newPwd),MD5Util.getStringMD5(oldPwd))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseCallback<BaseEntery>() {
                        @Override
                        public void onSuccess(BaseEntery value) {
                            RxToast.error("修改成功");
                            finish();
                        }

                        @Override
                        public void onFailture(String e) {
                            RxToast.error(e);
                        }
                    });
        }else if(id == R.id.back ){
            finish();
        }
    }
}
