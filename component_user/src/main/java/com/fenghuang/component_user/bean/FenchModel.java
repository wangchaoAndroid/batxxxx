package com.fenghuang.component_user.bean;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/8/21 15:37
 */
public class FenchModel implements Serializable{
    public int meter;
    public double latitude;
    public double longitude;

    @Override
    public String toString() {
        return "FenchModel{" +
                "meter=" + meter +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
