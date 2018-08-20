package com.fenghuang.component_user;

import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.utils.ContextManager;

import java.util.List;

/**
 * Create by wangchao on 2018/8/13 10:11
 */
public final class UserManager {
    private static LoginModel userInfo;

    public static void saveUserInfo(LoginModel loginModel){
        userInfo  = loginModel;
    }

    public static void clearUserInfo(){
        userInfo  = null;
    }

    public static LoginModel getUserInfo(){
        return userInfo;
    }

    public static String getToken(){
        if(userInfo == null){
            return (String) SPDataSource.get(ContextManager.getAppContext(),SPDataSource.USER_TOKEN,"");
        }
        return userInfo.token;
    }
}
