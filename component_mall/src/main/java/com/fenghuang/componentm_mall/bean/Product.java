package com.fenghuang.componentm_mall.bean;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/7/21 09:53
 */
public class Product implements Serializable {
    public int id;

    public String img;

    public String batteryModel;

    public int isStages;

    public long createTime;

    public int status;

    public String batteryName;

    public String describe1;

    public String describe2;

    public String describe3;

    public String describe4;

    public String shopName;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", batteryModel='" + batteryModel + '\'' +
                ", isStages=" + isStages +
                ", createTime=" + createTime +
                ", status=" + status +
                ", batteryName='" + batteryName + '\'' +
                ", describe1='" + describe1 + '\'' +
                ", describe2='" + describe2 + '\'' +
                ", describe3='" + describe3 + '\'' +
                ", describe4='" + describe4 + '\'' +
                ", shopName='" + shopName + '\'' +
                '}';
    }
}
