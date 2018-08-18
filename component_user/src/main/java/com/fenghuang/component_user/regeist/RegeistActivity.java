package com.fenghuang.component_user.regeist;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.MD5Util;
import com.fenghuang.component_base.utils.ViewFinder;
import com.fenghuang.component_user.LoginModel;
import com.fenghuang.component_user.NetServices;
import com.fenghuang.component_user.R;
import com.fenghuang.component_user.login.LoginActivity;
import com.fenghuang.component_user.view.TimeCount;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wyl on 2017/12/5
 */
public class RegeistActivity extends BaseActivity implements View.OnClickListener {

    private TextInputEditText tiet_phone;
    private TextInputEditText tiet_password,tiet_nick,tiet_auth_code,tiet_re_password;
    private TextView btn_login;
    private String callId;
    private TextView tvCode;
    private TimeCount time;
    private NetServices mNetServices;

    @Override
    public void initView() {
        ViewFinder viewFinder = new ViewFinder(this);
        tiet_phone = viewFinder.find(R.id.tiet_phone);
        tiet_password = viewFinder.find(R.id.tiet_password);
        tiet_nick = viewFinder.find(R.id.tiet_nick);
        tiet_auth_code = viewFinder.find(R.id.tiet_auth_code);
        tiet_re_password = viewFinder.find(R.id.tiet_re_password);
        addOnClickListeners(R.id.btn_login);
        tvCode = (TextView) findViewById(R.id.code);
        tvCode.setOnClickListener(this);
        time = new TimeCount(60000,1000,tvCode);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        callId = getIntent().getStringExtra("callId");
        mNetServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    }


    @Override
    protected void setEvent() {
//        etUsername.addTextChangedListener(mTextWatcher);
//        addOnClickListeners(R.id.iv_arrow, R.id.iv_visibility, R.id.btn_login, R.id.tv_login_config);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_activity_regeist;
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        String phone = tiet_phone.getText().toString().trim();
        String pwd = tiet_password.getText().toString().trim();
        String nickName = tiet_nick.getText().toString().trim();
        String auth_code = tiet_auth_code.getText().toString().trim();
        String re_password = tiet_re_password.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            RxToast.error("请输入手机号");
            return;
        }

        //注册
        if(id == R.id.btn_login) {
            if(TextUtils.isEmpty(auth_code)){
                RxToast.error("请输入验证码");
                return;
            }
            if(TextUtils.isEmpty(nickName)){
                RxToast.error("请输入昵称");
                return;
            }
            if(TextUtils.isEmpty(pwd)){
                RxToast.error("请输入密码");
                return;
            }
            if(TextUtils.isEmpty(re_password)){
                RxToast.error("请再次输入密码");
                return;
            }

            if(!re_password.equals(pwd)){
                RxToast.error("密码输入不一致，请重新输入");
                return;
            }
            mNetServices.regeist(phone, MD5Util.getStringMD5(pwd),nickName,auth_code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseCallback<BaseEntery>() {
                        @Override
                        public void onSuccess(BaseEntery value) {
                            startActivity(LoginActivity.class,true);
                        }

                        @Override
                        public void onFailture(String e) {
                            RxToast.error(e);
                        }
                    });
        } else if (id == R.id.tv_login_config) {

        }else if(id == R.id.code ){
            getCode(phone);
            time.start();
        }
    }


    public void getCode(String phone) {
        mNetServices.sendCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<String>>() {
                    @Override
                    public void onSuccess(BaseEntery<String> value) {
                        RxToast.normal("已发送");
                    }

                    @Override
                    public void onFailture(String e) {
                        //RxToast.error(e);
                    }
                });
    }


//    private void showPopupWindow(View view) {
//        if (mAllSavedUser.size() != 0) {
//            if (mUserListPopup == null) {
//                initUserListPopup();
//            }
//            if (!mUserListPopup.isShowing()) {
//                long delayMillis = 0;
//                if (isSoftShow) {
//                    // 如果软键盘是弹出状态先隐藏软键盘
//                    delayMillis = 100;
//                    BasicBiz.hideKeyboard(view);
//                }
//                mHandler.postDelayed(() -> {
//                    arrowUp();
//                    int yOff = (llUsername.getHeight() - rlUsername.getHeight()) / 2;
//                    mUserListPopup.showAsDropDown(rlUsername, 0, yOff);
//                }, delayMillis);
//            }
//        }
//    }
//
//
//    /**
//     * 箭头向上转的动画
//     */
//    private void arrowUp() {
//        if (mUpAnim == null) {
//            mUpAnim = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f);
//            mUpAnim.setDuration(300);
//            mUpAnim.setFillAfter(true);
//            mUpAnim.setInterpolator(new AccelerateDecelerateInterpolator());
//        }
//        ivArrow.startAnimation(mUpAnim);
//    }
//
//    /**
//     * 箭头向下转的动画
//     */
//    private void arrowDown() {
//        if (mDownAnim == null) {
//            mDownAnim = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF, 0.5f);
//            mDownAnim.setDuration(300);
//            mDownAnim.setFillAfter(true);
//            mDownAnim.setInterpolator(new AccelerateDecelerateInterpolator());
//        }
//        ivArrow.startAnimation(mDownAnim);
//    }
//
//    /**
//     * 登录中，显示dialog
//     */
//    public void onLogin() {
//        // 隐藏软键盘
//        BasicBiz.hideKeyboard(etUsername);
//        // 登陆中，显示dialog
//        if (customDialog == null) {
//            customDialog = new CustomDialog.StateBuilder(mContext)
//                    .setStateText(R.string.dialog_on_login)
//                    .setIrrevocable()
//                    .create();
//        } else if (customDialog.isShowing()) {
//            customDialog.dismiss();
//        }
//        customDialog.show();
//    }
//
//
//    public void loginSuccess() {
//        // 隐藏dialog
//        if (!isEmpty(callId)) {
//            CC.obtainBuilder("ComponentApp")
//                    .setContext(this)
//                    .setActionName("toMainActivity")
//                    .setTimeout(3000)
//                    .build()
//                    .callAsyncCallbackOnMainThread((cc, result) -> hideDialog());
//        } else {
//            startActivity(UserActivity.class, true);
//        }
//    }
//
//    public void loginFailed(int loginErrorCode) {
//        // 隐藏dialog
//        hideDialog();
//        switch (loginErrorCode) {
//            case LoginErrorCode.CAN_NOT_EMPTY:
//                showToast(R.string.toast_complete_login_data);
//                break;
//            case LoginErrorCode.NO_IP_AND_PORT:
//                showToast(R.string.toast_need_config_ip_port);
//                break;
//            case LoginErrorCode.LOGIN_FAILED:
//                showToast(R.string.toast_login_failed);
//                break;
//            case LoginErrorCode.WRONG_USER_INFO:
//                showToast(R.string.toast_incorrect_username_pwd);
//                break;
//            case LoginErrorCode.MQ_CONNECT_FAILED:
//                showToast(R.string.toast_mq_connect_failed);
//                break;
//        }
//    }
//
//    /**
//     * 隐藏状态对话框
//     */
//    private void hideDialog() {
//        if (customDialog != null && customDialog.isShowing()) {
//            customDialog.dismiss();
//        }
//    }
//

}
