package com.fenghuang.componentm_mall.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/11 0011.
 */

public class PayType implements Serializable {
    public int id;
    public int alarmnum;
    public double alarmmoney;
    public double downpayment;
    public String batteryName;
    public String staging;

    @Override
    public String toString() {
        return "PayType{" +
                "id=" + id +
                ", alarmnum=" + alarmnum +
                ", alarmmoney=" + alarmmoney +
                ", downpayment=" + downpayment +
                ", batteryName='" + batteryName + '\'' +
                ", staging='" + staging + '\'' +
                '}';
    }
}
