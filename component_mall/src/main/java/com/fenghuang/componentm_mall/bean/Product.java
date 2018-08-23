package com.fenghuang.componentm_mall.bean;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/7/21 09:53
 */
public class Product implements Serializable {
    public int id;

    public String batteryName;

    public String batteryModel;

    public int isStages;

    public String stagesNumber;

    public String stagesMoney;

    public long createTime;

    public int status;

    public int  shopId;

    public String batterytdescribe;

    @Override
    public String toString() {
        return "GoodModel{" +
                "id=" + id +
                ", batteryName='" + batteryName + '\'' +
                ", batteryModel='" + batteryModel + '\'' +
                ", isStages=" + isStages +
                ", stagesNumber='" + stagesNumber + '\'' +
                ", stagesMoney='" + stagesMoney + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", shopId=" + shopId +
                ", batterytdescribe='" + batterytdescribe + '\'' +
                '}';
    }
}
