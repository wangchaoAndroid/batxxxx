package com.fenghuang.component_user;

import com.fenghuang.component_base.net.BaseEntery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/8/13 09:19
 */
public class LoginModel extends BaseEntery  {
    public ArrayList<String> viceCardListNumber;
    public String nickName;
    public String sex;
    public String token;

    @Override
    public String toString() {
        return "LoginModel{" +
                "viceCardListNumber=" + viceCardListNumber +
                ", nickName='" + nickName + '\'' +
                ", sex='" + sex + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
