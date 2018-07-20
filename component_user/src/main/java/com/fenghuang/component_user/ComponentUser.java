package com.fenghuang.component_user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.fenghuang.component_base.data.CacheDataSource;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_user.login.LoginActivity;

public class ComponentUser implements IComponent {
    @Override
    public String getName() {
        //组件的名称，调用此组件的方式：
        // CC.obtainBuilder("ComponentA")...build().callAsync()
        return "component_user";
    }

    /**
     * 组件被调用时的入口
     * 要确保每个逻辑分支都会调用到CC.sendCCResult，
     * 包括try-catch,if-else,switch-case-default,startActivity
     * @param cc 组件调用对象，可从此对象中获取相关信息
     * @return true:将异步调用CC.sendCCResult(...),用于异步实现相关功能，例如：文件加载、网络请求等
     *          false:会同步调用CC.sendCCResult(...),即在onCall方法return之前调用，否则将被视为不合法的实现
     */
    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        if ("forceGetLoginUser".equals(actionName)) {
            String token = (String) SPDataSource.get(cc.getContext(), SPDataSource.USER_TOKEN, "");
            if (!TextUtils.isEmpty((token) )) {
                //已登录同步实现，直接调用CC.sendCCResult(...)并返回返回false
                CCResult result = CCResult.success(SPDataSource.USER_TOKEN, token);
                CC.sendCCResult(cc.getCallId(), result);
                return false;
            }
            //未登录，打开登录界面，在登录完成后再回调结果，异步实现
            Context context = cc.getContext();
            Intent intent = new Intent(context, LoginActivity.class);
            if (!(context instanceof Activity)) {
                //调用方没有设置context或app间组件跳转，context为application
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            //将cc的callId传给Activity，登录完成后通过这个callId来回传结果
            intent.putExtra("callId", cc.getCallId());
            context.startActivity(intent);
            if(context instanceof Activity){
                ((Activity) context).finish();
            }
            //异步实现，不立即调用CC.sendCCResult,返回true
            return true;
        }

        switch (actionName) {
            case "toLoginActivity":
                login(cc);
                break;
            case "toLoginActivityClearTask":
                loginClearTask(cc);
                break;
            case "showActivityA":
                break;
            case "getLifecycleFragment":
                //demo for provide fragment object to other component
                getLifecycleFragment(cc);
                break;
            case "lifecycleFragment.addText":
                lifecycleFragmentDoubleText(cc);
                break;
            case "getInfo":
                getInfo(cc);
                break;
            default:
                //这个逻辑分支上没有调用CC.sendCCResult(...),是一种错误的示例
                //并且方法的返回值为false，代表不会异步调用CC.sendCCResult(...)
                //在LocalCCInterceptor中将会返回错误码为-10的CCResult
                break;
        }
        return false;
    }

    private void lifecycleFragmentDoubleText(CC cc) {
        UserFragment lifecycleFragment = cc.getParamItem("fragment");
        if (lifecycleFragment != null) {
            String text = cc.getParamItem("text", "");
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        } else {
            CC.sendCCResult(cc.getCallId(), CCResult.error("no fragment params"));
        }
    }

    private void getLifecycleFragment(CC cc) {
        CC.sendCCResult(cc.getCallId(), CCResult.success("fragment", new UserFragment())
            .addData("int", 2)
        );
    }

    public void login(CC cc){
        Context context = cc.getContext();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("callId", cc.getCallId());
        if (!(context instanceof Activity)) {
            //调用方没有设置context或app间组件跳转，context为application
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
        CC.sendCCResult(cc.getCallId(), CCResult.success());
    }

    public void loginClearTask(CC cc){
        Context context = cc.getContext();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("callId", cc.getCallId());
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
    }

    private void getInfo(CC cc) {
        String userName = "billy";
        CC.sendCCResult(cc.getCallId(), CCResult.success("userName", userName));
    }

    private void openActivity(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, LoginActivity.class);
        if (!(context instanceof Activity)) {
            //调用方没有设置context或app间组件跳转，context为application
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
    }
}
