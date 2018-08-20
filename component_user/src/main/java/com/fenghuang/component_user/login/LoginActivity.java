package com.fenghuang.component_user.login;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.MD5Util;
import com.fenghuang.component_base.utils.ViewFinder;
import com.fenghuang.component_user.Contast;
import com.fenghuang.component_user.LoginModel;
import com.fenghuang.component_user.NetServices;
import com.fenghuang.component_user.R;
import com.fenghuang.component_user.UserActivity;
import com.fenghuang.component_user.UserManager;
import com.fenghuang.component_user.regeist.RegeistActivity;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.List;
import java.util.logging.Logger;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by wyl on 2017/12/5
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextInputEditText tiet_phone;
    private TextInputEditText tiet_password;
    private String callId;

    @Override
    public void initView() {
        ViewFinder viewFinder = new ViewFinder(this);
        tiet_phone = viewFinder.find(R.id.tiet_phone);
        tiet_password = viewFinder.find(R.id.tiet_password);
        addOnClickListeners(R.id.btn_login,R.id.tv_login_config,R.id.tv_forget_config);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        callId = getIntent().getStringExtra("callId");
    }

    @Override
    protected void setEvent() {

    }

    public void regeistXG(String phone ,String pwd){
        XGPushManager.registerPush(this,new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
//token在设备卸载重装的时候有可能会变
                ILog.e("TPush", "注册成功，设备token为：" + data);
                String xgToken = (String) data;
                login(phone, MD5Util.getStringMD5(pwd),xgToken);
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                ILog.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);

            }
        });

    }

    public void login(String phone ,String pwd,String xgToken){
        //测试账号，登录
        NetServices netServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
        netServices.login(phone,pwd,xgToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<LoginModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<LoginModel> value) {
                        ILog.e( "login" ,value + "");
                        LoginModel loginModel = value.obj;
                        //内存保存用户数据
                        UserManager.saveUserInfo(loginModel);
                        //单独保存token
                        SPDataSource.put(LoginActivity.this,SPDataSource.USER_TOKEN,loginModel.token);
                        //根据电池集合判读是否需要购买
                        List<String> viceCardListNumber = loginModel.viceCardListNumber;
                        if(ILog.DEBUG){
                            call = CC.obtainBuilder("component_app")
                                    .setContext(LoginActivity.this)
                                    .setActionName("enterMain")
                                    .build()
                                    .call();
                            if(call.isSuccess()){
                                finish();
                            }
                        }else {
                            if(viceCardListNumber != null && !viceCardListNumber.isEmpty()){
                                call = CC.obtainBuilder("component_app")
                                        .setContext(LoginActivity.this)
                                        .setActionName("enterMain")
                                        .build()
                                        .call();
                                if(call.isSuccess()){
                                    finish();
                                }
                            }else {
                                call = CC.obtainBuilder("component_mall")
                                        .setContext(LoginActivity.this)
                                        .setActionName("getBuyActivity")
                                        .build()
                                        .call();
                            }
                        }




                    }

                    @Override
                    public void onFailture(String e) {
                        ILog.e( "login" ,e + "");
                        RxToast.error(e);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_activity_login;
    }

    CCResult call;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_login) {
            String phone = tiet_phone.getText().toString().trim();
            String pwd = tiet_password.getText().toString().trim();
            if(TextUtils.isEmpty(phone)){
                RxToast.error("请输入手机号");
                return;
            }
            if(TextUtils.isEmpty(pwd)){
                RxToast.error("请输入密码");
                return;
            }
            regeistXG(phone,pwd);
        } else if (id == R.id.tv_login_config) {
            startActivity(RegeistActivity.class,false);
        }
        else if (id == R.id.tv_forget_config) {
            startActivity(ForgetPwdActivity.class,true);
        }
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
