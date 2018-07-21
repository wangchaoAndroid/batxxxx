package com.fenghuang.component_user.login;

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
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.utils.ViewFinder;
import com.fenghuang.component_user.R;
import com.fenghuang.component_user.UserActivity;
import com.fenghuang.component_user.regeist.RegeistActivity;

/**
 * Created by wyl on 2017/12/5
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextInputEditText tiet_phone;
    private TextInputEditText tiet_password;
    private TextView btn_login;
    private String callId;
    @Override
    public void initView() {
        ViewFinder viewFinder = new ViewFinder(this);
        tiet_phone = viewFinder.find(R.id.tiet_phone);
        tiet_password = viewFinder.find(R.id.tiet_password);
        addOnClickListeners(R.id.btn_login,R.id.tv_login_config);
    }




    /**
     * 获取所有保存过的用户信息
     */
    @Override
    public void initData() {
        callId = getIntent().getStringExtra("callId");
    }

    @Override
    protected void setEvent() {
//        etUsername.addTextChangedListener(mTextWatcher);
//        addOnClickListeners(R.id.iv_arrow, R.id.iv_visibility, R.id.btn_login, R.id.tv_login_config);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_activity_login;
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_login) {
            String phone = tiet_phone.getText().toString().trim();
            String pwd = tiet_password.getText().toString().trim();
            //测试账号，登录
            if("admin".equals(phone) && "admin".equals(pwd)){
                SPDataSource.put(this,SPDataSource.USER_TOKEN,"1111111");
                //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
                CC.obtainBuilder("component_app")
                        .setContext(this)
                        .setActionName("enterMain")
                        .build()
                        .call();
            }
        } else if (id == R.id.tv_login_config) {
            startActivity(RegeistActivity.class,false);
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
