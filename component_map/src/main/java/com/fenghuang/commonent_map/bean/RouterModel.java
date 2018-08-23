package com.fenghuang.commonent_map.bean;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/8/23 16:16
 */
public class RouterModel implements Serializable {
    public int id;
    public double longitude;
    public double latitude;
    public long createTime;
    public String productNumber;

    @Override
    public String toString() {
        return "RouterModel{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", createTime=" + createTime +
                ", productNumber='" + productNumber + '\'' +
                '}';
    }
}
