package com.fenghuang.component_user.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Create by wangchao on 2018/8/22 18:21
 */
public class BindModel implements Serializable ,IPickerViewData{
    public int id;
    public int isAdmin;
    public String createTime;
    public String productNumber;
    public String account;

    @Override
    public String getPickerViewText() {
        return productNumber;
    }
}
