package com.fenghuang.component_battery.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create by wangchao on 2018/8/20 14:55
 */
public class HomeModel {
    public int temperature;
    public int electricquantity;
    public double longitude;
    public double latitude;
    public List<Ad> advertisingtabList;
    public int railstatus; //是否开启围栏
    public int lockstatus;
    public Alarmtab alarmtab;

    public class Alarmtab implements Serializable{
        public int id;
        public String alarmtitle;
        public String alarmcontent;
        public int alarmtype;
        public String productNumber;
        public String crateTime;
        public int status;

        @Override
        public String toString() {
            return "Alarmtab{" +
                    "id=" + id +
                    ", alarmtitle='" + alarmtitle + '\'' +
                    ", alarmcontent='" + alarmcontent + '\'' +
                    ", alarmtype=" + alarmtype +
                    ", productNumber='" + productNumber + '\'' +
                    ", crateTime='" + crateTime + '\'' +
                    ", status=" + status +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HomeModel{" +
                "temperature=" + temperature +
                ", electricquantity=" + electricquantity +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", advertisingtabList=" + advertisingtabList +
                ", railstatus=" + railstatus +
                ", lockstatus=" + lockstatus +
                ", alarmtab=" + alarmtab +
                '}';
    }
}
