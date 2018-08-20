package com.fenghuang.component_user.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.bean.User;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.MD5Util;
import com.fenghuang.component_base.utils.ViewFinder;
import com.fenghuang.component_user.LoginModel;
import com.fenghuang.component_user.NetServices;
import com.fenghuang.component_user.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ForgetPwdActivity";
    ImageView back;
    TextView title;
    TextView next;

    EditText etPhone;
    EditText etAuthCode;
    EditText etPwd;
    TextView authCodeGet;

    private String auth_code;
    private Timer timer;
    NetServices netServices;

    public void back(){
        finish();
    }

    @Override
    protected void initView() {
        ViewFinder viewFinder = new ViewFinder(this);
        etPhone = viewFinder.find(R.id.et_phone);
        etAuthCode = viewFinder.find(R.id.et_auth_code);
        etPwd = viewFinder.find(R.id.et_pwd);
        authCodeGet = viewFinder.find(R.id.auth_code_get);
        title = viewFinder.find(R.id.title);
        addOnClickListeners(R.id.auth_code_get,R.id.sure,R.id.back);
    }





    @Override
    protected void initData(Bundle savedInstanceState) {
        title.setText("重置密码");
        authCodeGet.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        netServices= RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    }

    @Override
    protected void setEvent() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }


    private int currentTimer = 60;
    public void getAuthCode(){
        Log.e(TAG,"---" + currentTimer);
        if(currentTimer > 0  && currentTimer < 60){
            return;
        }
        String phone = etPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        netServices.sendCode(phone).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ResponseCallback<BaseEntery<String>>() {
                @Override
                public void onSuccess(BaseEntery<String> value) {
                    timer = new Timer();
                    timer.schedule(new TimeRunnable(currentTimer),0,1000);
                }

                @Override
                public void onFailture(String e) {

                }
            });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.back){
            back();
        }else if(id == R.id.auth_code_get){
            getAuthCode();
        }else if(id == R.id.sure){
            sure();
        }
    }

    public class TimeRunnable extends TimerTask {

        private int time;

        public TimeRunnable(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            time--;
            currentTimer = time;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(time == 0){
                        authCodeGet.setText("获取验证码");
                        currentTimer = 60;
                        timer.cancel();

                    }else {
                        authCodeGet.setText(time + "s");
                    }

                }
            });

        }
    }

    /**
     * 确定
     */
    public void sure(){
        String phone = etPhone.getText().toString().trim();
        String authCode = etAuthCode.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            Toast.makeText(this,"请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }

        reset(phone,authCode,pwd);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }
    }

    private void reset(String phone, String authCode,String newPassWord) {
        if (isDialogShowing()) {
            return;
        }
        showLoadingDialog();
        String md5Pwd = MD5Util.getStringMD5(newPassWord);
        netServices.retrievePassword(phone, authCode, md5Pwd).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {
                        // 保存用户信息等操作
                        ILog.e(TAG, value.toString());
                        RxToast.info("重置成功");
                        startActivity(new Intent(ForgetPwdActivity.this, LoginActivity.class));
                        finish();
                        dimissLoadingDialog();
                    }

                    @Override
                    public void onFailture(String e) {
                        dimissLoadingDialog();
                    }
                });
    }
}
