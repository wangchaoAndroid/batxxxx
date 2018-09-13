package com.fenghuang.commonent_map.bean;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/8/28 09:23
 */
public class PositionModel implements Serializable {
    public int railstatus; //是否开启围栏
    public int lockstatus; //是否死锁
    public int status; //开关状态
    public int meter;
    public double latitude;
    public double longitude;
    @Override
    public String toString() {
        return "PositionModel{" +
                "railstatus=" + railstatus +
                ", lockstatus=" + lockstatus +
                ", status=" + status +
                '}';
    }
}
