package com.fenghuang.component_battery.bean;

import java.util.List;

/**
 * Create by wangchao on 2018/8/20 14:55
 */
public class HomeModel {
    public String temperature;
    public String electricquantity;
    public double longitude;
    public double latitude;
    public List<Ad> advertisingtabList;

    @Override
    public String toString() {
        return "HomeModel{" +
                "temperature='" + temperature + '\'' +
                ", electricquantity='" + electricquantity + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", advertisingtabList=" + advertisingtabList +
                '}';
    }
}
