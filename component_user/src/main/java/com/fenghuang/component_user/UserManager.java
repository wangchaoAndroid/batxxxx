package com.fenghuang.component_user;

import java.util.List;

/**
 * Create by wangchao on 2018/8/13 10:11
 */
public final class UserManager {
    private static LoginModel userInfo;

    public static void saveUserInfo(LoginModel loginModel){
        userInfo  = loginModel;
    }

    public static LoginModel getUserInfo(){
        return userInfo;
    }
}
