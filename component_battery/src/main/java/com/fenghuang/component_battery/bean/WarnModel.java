package com.fenghuang.component_battery.bean;

/**
 * Create by wangchao on 2018/8/20 13:39
 */
public class WarnModel {
    public long id;
    public String alarmtitle;
    public int alarmtype;
    public String alarmcontent;
    public String productNumber;
    public String crateTime;
    public int  status;

    @Override
    public String toString() {
        return "WarnModel{" +
                "id=" + id +
                ", alarmtitle='" + alarmtitle + '\'' +
                ", alarmtype=" + alarmtype +
                ", productNumber='" + productNumber + '\'' +
                ", crateTime='" + crateTime + '\'' +
                ", status=" + status +
                '}';
    }
}
